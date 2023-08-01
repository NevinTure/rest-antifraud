package rest.antifraud.services;

import rest.antifraud.models.Card;
import rest.antifraud.repositories.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    public void save(Card card) {
        cardRepository.save(card);
    }

    public void deleteByNumber(String number) {
        cardRepository.deleteByNumber(number);
    }

    //TODO сделать аннотацию
    public boolean checkNumber(String number) {
        String regex = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
        return number.matches(regex);
    }
}
