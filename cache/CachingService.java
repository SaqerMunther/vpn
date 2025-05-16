// src/main/java/com/arabbank/hdf/digitalbackend/digital/configuration/cache/CachingService.java
package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;
import com.arabbank.hdf.digitalbackend.digital.constant.helios.HeliosConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.mdm.MdmConstants;
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
import com.arabbank.hdf.digitalbackend.digital.service.sme.card.SmeCardsServiceImp;
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
        refreshPerCountry(SmeConstants.SME_COUNTRY_LIST, SmeChartsServiceImp.class);
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
        refreshPerCountry(MdmConstants.COUNTRIES_LIST, MdmChartsServiceImp.class);
        log.debug("Refresh MDM Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllHELIOSCachesAtIntervals() {
        log.debug("Refresh HELIOS Cache started");
        refreshPerCountry(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosChartsServiceImp.class);
        refreshPerCountry(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosConnectionCardService.class);
        refreshPerCountry(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosActiveUsersCardService.class);
        log.debug("Refresh HELIOS Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllEABCachesAtIntervals() {
        log.debug("Refresh EAB Cache started");
        refreshGeneric(Stream.of(ChartServiceImpl.class, BarChartServiceImp.class));
        refreshPerKeyList(HeliosConstants.EAB_CARDS_LIST, ActiveUsersAndHitRegistrationsCardServiceImpl.class);
        refreshPerKeyList(HeliosConstants.EAB_REGISTRATIONS_CARD, RegistrationCardService.class);
        refreshPerKeyList(HeliosConstants.EAB_CONNECTION_CARD, ConnectionCardService.class);
        log.debug("Refresh EAB Cache finished");
    }

    @Async
    @Scheduled(cron = "0/30 * * * * *")
    public void refreshAllOMNIFYCachesAtIntervals() {
        log.debug("Refresh OMNIFY Cache started");
        refreshPerKeyList(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyChartsServiceImp.class);
        refreshPerKeyList(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyConnectionCardService.class);
        refreshPerKeyList(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyServersServiceImpl.class);
        log.debug("Refresh OMNIFY Cache finished");
    }

    private <T extends CacheableService<?, ?>> void refreshGeneric(Stream<Class<T>> services) {
        services.map(clazz -> applicationContext.getBean(clazz))
                .forEach(svc -> updateCache(svc.getCacheName(), svc.getCacheKey(), () -> svc.findInstanceData("")));
    }

    private <T extends CacheableService<?, ?>> void refreshAllMdmCaches() {
        refreshGeneric(Stream.of(
            MDMCardsServiceImp.class,
            NodeTrxServiceImp.class,
            NodeTrxVitalsServiceImp.class,
            MdmCherriesServiceImp.class,
            StatusDialogServiceImp.class
        ));
    }

    private <T extends CacheableService<?, ?>> void refreshAllSMECaches() {
        refreshGeneric(Stream.of(SmeServersServiceImpl.class));
    }

    private <T extends CacheableService<?, ?>> void refreshPerCountry(List<String> keys, Class<T> clazz) {
        T svc = applicationContext.getBean(clazz);
        keys.forEach(key -> updateCache(key + svc.getCacheName(), key + svc.getCacheKey(), () -> svc.findInstanceData(key)));
    }

    private <T extends CacheableService<?, ?>> void refreshPerKeyList(List<String> keys, Class<T> clazz) {
        refreshPerCountry(keys, clazz);
    }

    private void updateCache(String cacheName, Object cacheKey, DataSupplier supplier) {
        if (Objects.nonNull(cacheKey)) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                cache.put(cacheKey, supplier.get());
                log.debug("Cache [{}] updated for key: {}", cacheName, cacheKey);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @FunctionalInterface
    private interface DataSupplier {
        Object get();
    }
}







The method refreshGeneric(Stream<Class<T>>) in the type CachingService is not applicable for the arguments (Stream<Class<? extends StandardCacheableService<String>>>)
