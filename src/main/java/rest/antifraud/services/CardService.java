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

}
