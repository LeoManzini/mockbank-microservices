package br.com.leomanzini.cards.service.impl;

import br.com.leomanzini.cards.dto.CardsDto;
import br.com.leomanzini.cards.entity.Cards;
import br.com.leomanzini.cards.exception.CardAlreadyExistsException;
import br.com.leomanzini.cards.exception.ResourceNotFoundException;
import br.com.leomanzini.cards.mapper.CardsMapper;
import br.com.leomanzini.cards.repository.CardsRepository;
import br.com.leomanzini.cards.service.CardsServiceInterface;
import br.com.leomanzini.cards.utils.CardsConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements CardsServiceInterface {

    private final CardsRepository cardsRepository;

    @Override
    public Long createCard(String mobileNumber) {
        Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
        if (optionalCards.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        return cardsRepository.save(createNewCard(mobileNumber)).getCardId();
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public void updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
    }

    @Override
    public void deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.deleteById(cards.getCardId());
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
