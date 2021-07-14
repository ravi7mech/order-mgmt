package com.apptium.service.impl;

import com.apptium.domain.OrdProduct;
import com.apptium.repository.OrdProductRepository;
import com.apptium.service.OrdProductService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdProduct}.
 */
@Service
@Transactional
public class OrdProductServiceImpl implements OrdProductService {

    private final Logger log = LoggerFactory.getLogger(OrdProductServiceImpl.class);

    private final OrdProductRepository ordProductRepository;

    public OrdProductServiceImpl(OrdProductRepository ordProductRepository) {
        this.ordProductRepository = ordProductRepository;
    }

    @Override
    public OrdProduct save(OrdProduct ordProduct) {
        log.debug("Request to save OrdProduct : {}", ordProduct);
        return ordProductRepository.save(ordProduct);
    }

    @Override
    public Optional<OrdProduct> partialUpdate(OrdProduct ordProduct) {
        log.debug("Request to partially update OrdProduct : {}", ordProduct);

        return ordProductRepository
            .findById(ordProduct.getId())
            .map(
                existingOrdProduct -> {
                    if (ordProduct.getVersionId() != null) {
                        existingOrdProduct.setVersionId(ordProduct.getVersionId());
                    }
                    if (ordProduct.getVariationId() != null) {
                        existingOrdProduct.setVariationId(ordProduct.getVariationId());
                    }
                    if (ordProduct.getLineOfService() != null) {
                        existingOrdProduct.setLineOfService(ordProduct.getLineOfService());
                    }
                    if (ordProduct.getAssetId() != null) {
                        existingOrdProduct.setAssetId(ordProduct.getAssetId());
                    }
                    if (ordProduct.getSerialNo() != null) {
                        existingOrdProduct.setSerialNo(ordProduct.getSerialNo());
                    }
                    if (ordProduct.getName() != null) {
                        existingOrdProduct.setName(ordProduct.getName());
                    }

                    return existingOrdProduct;
                }
            )
            .map(ordProductRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdProduct> findAll() {
        log.debug("Request to get all OrdProducts");
        return ordProductRepository.findAll();
    }

    /**
     *  Get all the ordProducts where OrdOrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdProduct> findAllWhereOrdOrderItemIsNull() {
        log.debug("Request to get all ordProducts where OrdOrderItem is null");
        return StreamSupport
            .stream(ordProductRepository.findAll().spliterator(), false)
            .filter(ordProduct -> ordProduct.getOrdOrderItem() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdProduct> findOne(Long id) {
        log.debug("Request to get OrdProduct : {}", id);
        return ordProductRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdProduct : {}", id);
        ordProductRepository.deleteById(id);
    }
}
