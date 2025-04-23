package com.nttdata.creditproducts.service.kafka.listener;

import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.exception.RemoteServiceUnavailableException;
import com.nttdata.creditproducts.service.model.AssociateCardRequest;
import com.nttdata.creditproducts.service.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CredicardProductRequest;
import org.openapitools.model.CreditCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class CardAssociationListener {

    private static final Logger logger = LoggerFactory.getLogger(CardAssociationListener.class);
    private final KafkaTemplate<String, CreditCard> kafkaTemplate;
    private final CreditCardService creditCardService;
    @KafkaListener(topics = "wallet-associate-card-topic",
            groupId = "card-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveAssociationRequest(AssociateCardRequest request) {
        logger.info("Recibido AssociateCardRequest: DNI={}, Tarjeta={}",
                request.getDni(), request.getCardNumber());
        validateRequest(request);
        findMatchingCard(request)
                .subscribe(
                        card -> processCardMatch(card, request),
                        error -> handleCardAssociationError(error, request)
                );
    }


    private void validateRequest(AssociateCardRequest request) {
        if (request.getCardNumber() == null || request.getCardNumber().isBlank()) {
            throw new BusinessException("The card number cannot be empty.");
        }
    }

    private Mono<CreditCard> findMatchingCard(AssociateCardRequest request) {
        CredicardProductRequest productRequest = new CredicardProductRequest();
        productRequest.setDni(request.getDni());

        return creditCardService.getAllProductUser(productRequest)
                .flatMapMany(HttpEntity::getBody)
                .filter(card -> {
                    assert request.getCardNumber() != null;
                    String reqCard = request.getCardNumber().replaceAll("[^\\d]", "");
                    String dbCard = card.getCardNumber().replaceAll("[^\\d]", "");
                    return reqCard.equalsIgnoreCase(dbCard);
                })
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(new BusinessException("Card with number not found " +
                        request.getCardNumber() + " for the ID " + request.getDni())));
    }

    private void processCardMatch(CreditCard card, AssociateCardRequest request) {
        if (!"DEBITO".equalsIgnoreCase(card.getTypeCard().getValue())) {
            throw new BusinessException("The card" + request.getCardNumber() + " It is not a DEBIT type. ");
        }
        kafkaTemplate.send("wallet-associate-card-topic-response", card);
        logger.info("Debit card sent to the reply topic.");
    }

    private void handleCardAssociationError(Throwable error, AssociateCardRequest request) {
        logger.error("Error associating card with DNI {}", request.getDni(), error);
        if (!(error instanceof BusinessException)) {
            throw new RemoteServiceUnavailableException("Card service failure");
        }
    }
}