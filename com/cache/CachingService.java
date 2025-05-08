// src/main/java/com/arabbank/hdf/digitalbackend/digital/configuration/cache/CacheConfiguration.java
package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.arabbank.hdf.digitalbackend.digital.constant.helios.HeliosConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.mdm.MdmConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.omnify.OmnifyConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.sme.SmeConstants;
import com.arabbank.hdf.digitalbackend.digital.repository.sqlserver.omnify.OmnifyServiceRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableCaching
@EnableScheduling   // <— needed to enable @Scheduled
@RequiredArgsConstructor
public class CacheConfiguration {
    private final OmnifyServiceRepository omnifyServiceRepository;

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(getAllCacheNames());
        return cacheManager;
    }

    private List<String> getAllCacheNames() {
        List<String> baseCacheNames = Arrays.asList(
            HeliosConstants.EAB_CHARTS_CACHE_NAME,
            HeliosConstants.EAB_BARCHARTS_CACHE_NAME,
            SmeConstants.SME_SERVER_CACHE_NAME
        );

        List<String> allInstance = new ArrayList<>();
        allInstance.add(HeliosConstants.EAB_CHARTS_CACHE_NAME);
        allInstance.add(HeliosConstants.EAB_BARCHARTS_CACHE_NAME);

        allInstance.addAll(getCardWithPrefix(HeliosConstants.EAB_CARD_CACHE_NAME, HeliosConstants.EAB_CARDS_LIST));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.EAB_CARD_CACHE_NAME, HeliosConstants.EAB_CONNECTION_CARD));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.EAB_CARD_CACHE_NAME, HeliosConstants.EAB_REGISTRATIONS_CARD));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.HELIOS_CHARTS_CACHE_NAME, HeliosConstants.HELIOS_COUNTRY_LIST));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.HELIOS_CONNICTION_CARD_CACHE_NAME, HeliosConstants.HELIOS_COUNTRY_LIST));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.HELIOS_ACTIVEUSER_CARD_CACHE_NAME, HeliosConstants.HELIOS_COUNTRY_LIST));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.HELIOS_SERVER_CACHE_NAME, HeliosConstants.HELIOS_COUNTRY_LIST));

        allInstance.add(OmnifyConstants.OMNIFY_TOP_COMPANIES_CHARTS_CACHE_NAME);
        allInstance.add(OmnifyConstants.OMNIFY_PRODUCTS_CARD_CACHE_NAME);
        allInstance.addAll(getCardWithPrefix(OmnifyConstants.OMNIFY_CHARTS_CACHE_NAME, omnifyServiceRepository.getOmnifyCompanyNames()));
        allInstance.addAll(getCardWithPrefix(OmnifyConstants.OMNIFY_CONNICTION_CARD_CACHE_NAME, omnifyServiceRepository.getOmnifyCompanyNames()));
        allInstance.addAll(getCardWithPrefix(HeliosConstants.HELIOS_SERVER_CACHE_NAME, omnifyServiceRepository.getOmnifyCompanyNames()));

        allInstance.addAll(getCardWithPrefix(SmeConstants.SME_CHARTS_CACHE_NAME, SmeConstants.SME_COUNTRY_LIST));
        allInstance.addAll(Arrays.asList("cardsData","trxserver","serversvitals","packets","statusdialog"));
        allInstance.addAll(getCardWithPrefix("MdmChartsData", MdmConstants.COUNTRIES_LIST));

        return Stream.concat(baseCacheNames.stream(), allInstance.stream())
                     .collect(Collectors.toList());
    }

    public static List<String> getCardWithPrefix(String name, List<String> list) {
        return list.stream().map(card -> card + name).collect(Collectors.toList());
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new TimeBasedKeyGenerator();
    }
}







-----------------------------------








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
import com.arabbank.hdf.digitalbackend.digital.service.sme.chart.SmeChartsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.sme.server.SmeServersServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CachingService implements ApplicationContextAware {
    private final CacheManager cacheManager;
    private final OmnifyServiceRepository omnifyServiceRepository;
    private ApplicationContext applicationContext;

    // ─── EAB & HELIOS (every 30s) ────────────────────────────────────────────────
    @Scheduled(cron = "0/30 * * * * *", zone = "Asia/Amman")
    public void refreshHeliosCaches() {
        log.debug("► Refreshing EAB & HELIOS caches");
        // charts
        refreshAllCaches();
        // cards
        refreshNodeCaches(HeliosConstants.EAB_CARDS_LIST, ActiveUsersAndHitRegistrationsCardServiceImpl.class);
        refreshNodeCaches(HeliosConstants.EAB_REGISTRATIONS_CARD, RegistrationCardService.class);
        refreshNodeCaches(HeliosConstants.EAB_CONNECTION_CARD, ConnectionCardService.class);
        // per-country
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosChartsServiceImp.class);
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosConnectionCardService.class);
        refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosActiveUsersCardService.class);
    }

    // ─── OMNIFY (every 30s) ─────────────────────────────────────────────────
    @Scheduled(cron = "0/30 * * * * *", zone = "Asia/Amman")
    public void refreshOmnifyCaches() {
        log.debug("► Refreshing OMNIFY caches");
        refreshAllOmnifyCaches();
        refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyChartsServiceImp.class);
        refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyConnectionCardService.class);
        // your original call for servers
        refreshHeliosPerCountryCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyServersServiceImpl.class);
    }

    // ─── MDM (every 30s) ───────────────────────────────────────────────────
    @Scheduled(cron = "0/30 * * * * *", zone = "Asia/Amman")
    public void refreshMdmCaches() {
        log.debug("► Refreshing MDM caches");
        refreshAllMdmCaches();
        refreshMdmCountriesCaches(MdmConstants.COUNTRIES_LIST, MdmChartsServiceImp.class);
    }

    // ─── SME (every 30s) ───────────────────────────────────────────────────
    @Scheduled(cron = "0/30 * * * * *", zone = "Asia/Amman")
    public void refreshSmeCaches() {
        log.debug("► Refreshing SME caches");
        refreshAllSMECaches();
        refreshSmePerCountryCaches(SmeConstants.SME_COUNTRY_LIST, SmeChartsServiceImp.class);
    }

    // ─── EXISTING HELPERS (unchanged) ─────────────────────────────────────────
    @SuppressWarnings("DataFlowIssue")
    public void refreshAllCaches() {
        Stream<CacheableService<?, ?>> services = Stream
            .of(ChartServiceImpl.class, BarChartServiceImp.class)
            .map(applicationContext::getBean);
        services.forEach(svc -> {
            Cache c = cacheManager.getCache(svc.getCacheName());
            if (svc.getCacheKey() != null && c != null) {
                Object data = svc.findInstanceData("");
                c.clear();
                c.put(svc.getCacheKey(), data);
                log.debug("Updated cache '{}' key={}", svc.getCacheName(), svc.getCacheKey());
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllOmnifyCaches() {
        Stream<CacheableService<?, ?>> services = Stream
            .of(OmnifyTopCompaniesChartsServiceImp.class, OmnifyProductsCardService.class)
            .map(applicationContext::getBean);
        services.forEach(svc -> {
            Cache c = cacheManager.getCache(svc.getCacheName());
            if (svc.getCacheKey() != null && c != null) {
                Object data = svc.findInstanceData("");
                c.clear();
                c.put(svc.getCacheKey(), data);
                log.debug("Updated cache '{}' key={}", svc.getCacheName(), svc.getCacheKey());
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllMdmCaches() {
        Stream<CacheableService<?, ?>> services = Stream
            .of(MDMCardsServiceImp.class, NodeTrxServiceImp.class, NodeTrxVitalsServiceImp.class, MdmCherriesServiceImp.class, StatusDialogServiceImp.class)
            .map(applicationContext::getBean);
        services.forEach(svc -> {
            Cache c = cacheManager.getCache(svc.getCacheName());
            if (svc.getCacheKey() != null && c != null) {
                Object data = svc.findInstanceData("");
                c.clear();
                c.put(svc.getCacheKey(), data);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshAllSMECaches() {
        Stream<CacheableService<?, ?>> services = Stream
            .of(SmeServersServiceImpl.class)
            .map(applicationContext::getBean);
        services.forEach(svc -> {
            Cache c = cacheManager.getCache(svc.getCacheName());
            if (svc.getCacheKey() != null && c != null) {
                Object data = svc.findInstanceData("");
                c.clear();
                c.put(svc.getCacheKey(), data);
                log.debug("[SME] Updated cache '{}' key={}", svc.getCacheName(), svc.getCacheKey());
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshNodeCaches(List<String> cards, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        cards.forEach(card -> {
            String name = card + svc.getCacheName();
            Object key = card + svc.getCacheKey();
            Cache c = cacheManager.getCache(name);
            if (key != null && c != null) {
                Object data = svc.findInstanceData(card);
                c.clear();
                c.put(key, data);
                log.debug("Updated cache '{}' key={}", name, key);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshHeliosPerCountryCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        countries.forEach(country -> {
            String name = country + svc.getCacheName();
            Object key = country + svc.getCacheKey();
            Cache c = cacheManager.getCache(name);
            if (key != null && c != null) {
                Object data = svc.findInstanceData(country);
                c.clear();
                c.put(key, data);
                log.debug("Updated cache '{}' key={}", name, key);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshOmnifyPerCompanyCaches(List<String> companies, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        companies.forEach(comp -> {
            String name = comp + svc.getCacheName();
            Object key = comp + svc.getCacheKey();
            Cache c = cacheManager.getCache(name);
            if (key != null && c != null) {
                Object data = svc.findInstanceData(comp);
                c.clear();
                c.put(key, data);
                log.debug("Updated cache '{}' key={}", name, key);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshMdmCountriesCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        countries.forEach(country -> {
            String name = country + svc.getCacheName();
            Object key = country + svc.getCacheKey();
            Cache c = cacheManager.getCache(name);
            if (key != null && c != null) {
                Object data = svc.findInstanceData(country);
                c.clear();
                c.put(key, data);
            }
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public void refreshSmePerCountryCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
        CacheableService<?, ?> svc = applicationContext.getBean(clazz);
        countries.forEach(country -> {
            String name = country + svc.getCacheName();
            Object key = country + svc.getCacheKey();
            Cache c = cacheManager.getCache(name);
            if (key != null && c != null) {
                Object data = svc.findInstanceData(country);
                c.clear();
                c.put(key, data);
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
    }
}
