package br.com.leomanzini.cards.service;

import br.com.leomanzini.cards.dto.CardsDto;

public interface CardsServiceInterface {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    Long createCard(String mobileNumber);

    /**
     * @param mobileNumber - Input mobile Number
     *  @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);

    /**
     * @param mobileNumber - Input Mobile Number
     * @param cardsDto - Card Details
     */
    void updateCard(String mobileNumber, CardsDto cardsDto);

    /**
     * @param mobileNumber - Input Mobile Number
     */
    void deleteCard(String mobileNumber);
}
