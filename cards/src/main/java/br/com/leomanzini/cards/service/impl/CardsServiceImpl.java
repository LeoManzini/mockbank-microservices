package br.com.leomanzini.cards.service.impl;

import br.com.leomanzini.cards.dto.CardsDto;
import br.com.leomanzini.cards.entity.Cards;
import br.com.leomanzini.cards.entity.enums.CardType;
import br.com.leomanzini.cards.exception.CardAlreadyExistsException;
import br.com.leomanzini.cards.exception.ResourceNotFoundException;
import br.com.leomanzini.cards.mapper.CardsMapper;
import br.com.leomanzini.cards.repository.CardsRepository;
import br.com.leomanzini.cards.service.CardsServiceInterface;
import br.com.leomanzini.cards.utils.CardsConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements CardsServiceInterface {

    private final CardsRepository cardsRepository;

    @Override
    @Transactional
    public Long createCard(String mobileNumber) {
        if (cardsRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        return cardsRepository.save(createNewCard(mobileNumber)).getCardId();
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    @Transactional
    public void updateCard(String mobileNumber, CardsDto cardsDto) {
        if (!mobileNumber.equals(cardsDto.getMobileNumber())) {
            throw new IllegalArgumentException("Mobile number in the request body does not match the path variable");
        }

        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
    }

    @Override
    @Transactional
    public void deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        cardsRepository.deleteById(cards.getCardId());
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardType.CREDIT);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
