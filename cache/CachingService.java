// src/main/java/com/arabbank/hdf/digitalbackend/digital/configuration/cache/CachingService.java
package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;
import com.arabbank.hdf.digitalbackend.digital.constant.eab.HeliosConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.helios.HeliosConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.mdm.MdmConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.omnify.OmnifyConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.sme.SmeConstants;
import com.arabbank.hdf.digitalbackend.digital.repository.sqlserver.omnify.OmnifyServiceRepository;
import com.arabbank.hdf.digitalbackend.digital.service.eab.card.ActiveUsersAndHitRegistrationsCardServiceImpl;
import com.arabbank.hdf.digitalbackend.digital.service.eab.card.ConnectionCardService;
import com.arabbank.hdf.digitalbackend.digital.service.eab.card.RegistrationCardService;
import com.arabbank.hdf.digitalbackend.digital.service.eab.chart.BarChartServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.eab.chart.ChartServiceImpl;
import com.arabbank.hdf.digitalbackend.digital.service.helios.card.HeliosActiveUsersCardService;
import com.arabbank.hdf.digitalbackend.digital.service.helios.card.HeliosConnectionCardService;
import com.arabbank.hdf.digitalbackend.digital.service.helios.chart.HeliosChartsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.cards.MDMCardsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.charts.MdmChartsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.cherries.MdmCherriesServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.nodetrxonly.data.NodeTrxServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.nodetrxvitals.data.NodeTrxVitalsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.mdm.statusdialog.StatusDialogServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.omnify.card.OmnifyConnectionCardService;
import com.arabbank.hdf.digitalbackend.digital.service.omnify.card.OmnifyProductsCardService;
import com.arabbank.hdf.digitalbackend.digital.service.omnify.chart.OmnifyChartsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.omnify.dialog.OmnifyTopCompaniesChartsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.omnify.server.OmnifyServersServiceImpl;
import com.arabbank.hdf.digitalbackend.digital.service.sme.cards.SmeCardsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.sme.chart.SmeChartsServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Slf4j
public class CachingService implements ApplicationContextAware {
    private final CacheManager cacheManager;
    private ApplicationContext applicationContext;
    private final OmnifyServiceRepository omnifyServiceRepository;
    private final SmeCardsServiceImp smeCardsServiceImp;

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllSMECachesAtIntervals() {
        log.debug("Refresh SME Cache started");
        refreshAllSMECaches();
        refreshSmePerCountryCaches(SmeConstants.SME_COUNTRY_LIST, SmeChartsServiceImp.class);
        for (String country : SmeConstants.SME_COUNTRY_LIST) {
            try { smeCardsServiceImp.getTransactions(country); } catch (IOException e) { throw new RuntimeException(e); }
            try { smeCardsServiceImp.getActiveUsers(country); } catch (IOException e) { throw new RuntimeException(e); }
            try { smeCardsServiceImp.getDigitalOnBoarding(country); } catch (IOException e) { throw new RuntimeException(e); }
            try { smeCardsServiceImp.getConnections(country); } catch (IOException e) { throw new RuntimeException(e); }
        }
        log.debug("Refresh SME Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllMDMCachesAtIntervals() {
        log.debug("Refresh MDM Cache started");
        refreshAllMdmCaches();
        refreshMdmCountriesCaches(MdmConstants.COUNTRIES_LIST, MdmChartsServiceImp.class);
        log.debug("Refresh MDM Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllHELIOSCachesAtIntervals() {
        log.debug("Refresh HELIOS Cache started");
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosChartsServiceImp.class);
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosConnectionCardService.class);
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosActiveUsersCardService.class);
        log.debug("Refresh HELIOS Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllEABCachesAtIntervals() {
        log.debug("Refresh EAB Cache started");
        refreshAllCaches();
        refreshNodeCaches(HeliosConstants.EAB_CARDS_LIST, ActiveUsersAndHitRegistrationsCardServiceImpl.class);
        refreshNodeCaches(HeliosConstants.EAB_REGISTRATIONS_CARD, RegistrationCardService.class);
        refreshNodeCaches(HeliosConstants.EAB_CONNECTION_CARD, ConnectionCardService.class);
        log.debug("Refresh EAB Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllOMNIFYCachesAtIntervals() {
        log.debug("Refresh OMNIFY Cache started");
        refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyChartsServiceImp.class);
        refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyConnectionCardService.class);
        refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyServersServiceImpl.class);
        log.debug("Refresh OMNIFY Cache finished");
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllCaches() {
        Stream<CacheableService<?, ?>> services = Stream.of(ChartServiceImpl.class, BarChartServiceImp.class)
            .map(name -> applicationContext.getBean(name));
        services.forEach(cacheable -> {
            Cache cache = cacheManager.getCache(cacheable.getCacheName());
            if (Objects.nonNull(cacheable.getCacheKey())) {
                Object data = cacheable.findInstanceData("");
                cache.clear();
                cache.put(cacheable.getCacheKey(), data);
                log.debug("Cache updated for cache key: {}", cacheable.getCacheKey());
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllMdmCaches() {
        Stream<CacheableService<?, ?>> services = Stream.of(
            MDMCardsServiceImp.class,
            NodeTrxServiceImp.class,
            NodeTrxVitalsServiceImp.class,
            MdmCherriesServiceImp.class,
            StatusDialogServiceImp.class
        ).map(name -> applicationContext.getBean(name));
        services.forEach(cacheable -> {
            Cache cache = cacheManager.getCache(cacheable.getCacheName());
            if (Objects.nonNull(cacheable.getCacheKey())) {
                Object data = cacheable.findInstanceData("");
                cache.clear();
                cache.put(cacheable.getCacheKey(), data);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllSMECaches() {
        CacheableService<?, ?> svc = applicationContext.getBean(SmeServersServiceImpl.class);
        Cache cache = cacheManager.getCache(svc.getCacheName());
        if (Objects.nonNull(svc.getCacheKey())) {
            Object data = svc.findInstanceData("");
            cache.clear();
            cache.put(svc.getCacheKey(), data);
            log.debug("[SME Servers] Cache updated for cache key: {}", svc.getCacheKey());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshNodeCaches(List<String> cards, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        cards.forEach(card -> {
            Cache cache = cacheManager.getCache(card + svc.getCacheName());
            if (Objects.nonNull(card + svc.getCacheKey())) {
                Object data = svc.findInstanceData(card);
                cache.clear();
                cache.put(card + svc.getCacheKey(), data);
                log.debug("Cache updated for cache key: {} with Name: {}", svc.getCacheKey(), card);
