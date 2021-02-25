package am.smarket.discountcardappcommon.service.impl;

import am.smarket.discountcardappcommon.model.Massage;
import am.smarket.discountcardappcommon.model.User;
import am.smarket.discountcardappcommon.repository.MassageRepository;
import am.smarket.discountcardappcommon.service.MassageService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MassageServiceImpl implements MassageService {

    private MassageRepository massageRepository;

    public MassageServiceImpl(MassageRepository massageRepository) {
        this.massageRepository = massageRepository;
    }

    @Override
    public void save(Massage massage) {
        massageRepository.save(massage);
    }

    @Override
    public List<Massage> findAll() {
        return massageRepository.findAll(Sort.by("date").descending());
    }

    @Override
    public Massage getOne(int id) {
        return massageRepository.getOne(id);
    }

    @Override
    public List<Massage> findAllByUser(User user) {
        return massageRepository.findAllByUsers(user);
    }

    @Override
    public void deletEmail(int id) {
        massageRepository.deleteById(id);
    }
}
