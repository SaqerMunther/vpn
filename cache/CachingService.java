package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import com.arabbank.hdf.digitalbackend.digital.constant.sme.*;
import com.arabbank.hdf.digitalbackend.digital.service.sme.card.SmeCardsServiceImp;
import com.arabbank.hdf.digitalbackend.digital.service.sme.chart.*;
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

import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;
import com.arabbank.hdf.digitalbackend.digital.constant.helios.HeliosConstants;
import com.arabbank.hdf.digitalbackend.digital.constant.mdm.MdmConstants;
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

	@Scheduled(cron = "0/30 * * * * *")
	public void refreshAllSMECachesAtIntervals() {
		log.debug("Refresh SME Cache started");
		refreshAllSMECaches();
		refreshSmePerCountryCaches(SmeConstants.SME_COUNTRY_LIST, SmeChartsServiceImp.class);
		for (int i = 0; i < SmeConstants.SME_COUNTRY_LIST.size(); i++) {

			try {
				smeCardsServiceImp.getTransactions(SmeConstants.SME_COUNTRY_LIST.get(i));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				smeCardsServiceImp.getActiveUsers(SmeConstants.SME_COUNTRY_LIST.get(i));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				smeCardsServiceImp.getDigitalOnBoarding(SmeConstants.SME_COUNTRY_LIST.get(i));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				smeCardsServiceImp.getConnections(SmeConstants.SME_COUNTRY_LIST.get(i));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		log.debug("Refresh SME Cache finished");
	}
	@Scheduled(cron = "0/30 * * * * *")
	public void refreshAllMDMCachesAtIntervals() {
		log.debug("Refresh MDM Cache started");
		refreshAllMdmCaches();
		refreshMdmCountriesCaches(MdmConstants.COUNTRIES_LIST, MdmChartsServiceImp.class);
		log.debug("Refresh MDM Cache finished");
	}
	@Scheduled(cron = "0/30 * * * * *")
	public void refreshAllHELIOSCachesAtIntervals() {
		log.debug("Refresh HELIOS Cache started");
		refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosChartsServiceImp.class);
		refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosConnectionCardService.class);
		refreshHeliosPerCountryCaches(HeliosConstants.HELIOS_COUNTRY_LIST, HeliosActiveUsersCardService.class);

		log.debug("Refresh HELIOS Cache finished");
	}
	@Scheduled(cron = "0/30 * * * * *")
	public void refreshAllEABCachesAtIntervals() {
		log.debug("Refresh EAB Cache started");
		refreshAllCaches();
		refreshNodeCaches(HeliosConstants.EAB_CARDS_LIST, ActiveUsersAndHitRegistrationsCardServiceImpl.class);
		refreshNodeCaches(HeliosConstants.EAB_REGISTRATIONS_CARD, RegistrationCardService.class);
		refreshNodeCaches(HeliosConstants.EAB_CONNECTION_CARD, ConnectionCardService.class);
		log.debug("Refresh EAB Cache finished");
	}
	@Scheduled(cron = "0/30 * * * * *")
	public void refreshAllOMNIFYCachesAtIntervals() {
		log.debug("Refresh OMNIFY Cache started");
		refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyChartsServiceImp.class);
		refreshOmnifyPerCompanyCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyConnectionCardService.class);
		refreshHeliosPerCountryCaches(omnifyServiceRepository.getOmnifyCompanyNames(), OmnifyServersServiceImpl.class);
		log.debug("Refresh OMNIFY Cache finished");
	}

	@SuppressWarnings("DataFlowIssue")
	public void refreshAllCaches() {
		Stream<CacheableService<?, ?>> cacheableServices = Stream
				.of(ChartServiceImpl.class, BarChartServiceImp.class)
				.map(serviceName -> applicationContext.getBean(serviceName));

		cacheableServices.forEach(cacheable -> {
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
	public void refreshAllOmnifyCaches() {
		Stream<CacheableService<?, ?>> cacheableServices = Stream
				.of(OmnifyTopCompaniesChartsServiceImp.class, OmnifyProductsCardService.class)
				.map(serviceName -> applicationContext.getBean(serviceName));

		cacheableServices.forEach(cacheable -> {
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
		Stream<CacheableService<?, ?>> cacheableServices = Stream
				.of(MDMCardsServiceImp.class,NodeTrxServiceImp.class ,NodeTrxVitalsServiceImp.class, MdmCherriesServiceImp.class,StatusDialogServiceImp.class)
				.map(serviceName -> applicationContext.getBean(serviceName));

        cacheableServices.forEach(cacheable -> {
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
		Stream<CacheableService<?, ?>> cacheableServices = Stream
				.of(SmeServersServiceImpl.class)
				.map(serviceName -> applicationContext.getBean(serviceName));

		cacheableServices.forEach(cacheable -> {
			Cache cache = cacheManager.getCache(cacheable.getCacheName());
			if (Objects.nonNull(cacheable.getCacheKey())) {
				Object data = cacheable.findInstanceData("");
				cache.clear();
				cache.put(cacheable.getCacheKey(), data);
				log.debug("[SME Servers] Cache updated for cache key: {}", cacheable.getCacheKey());
			}
		});
	}

	@SuppressWarnings("DataFlowIssue")
	public void refreshNodeCaches(List<String> cards, Class<? extends CacheableService<?, ?>> clazz) {
		CacheableService<?, ?> cacheableService = applicationContext.getBean(clazz);
		cards.forEach(card -> {

			Cache cache = cacheManager.getCache(card + cacheableService.getCacheName());
			if (Objects.nonNull(card + cacheableService.getCacheKey())) {
				Object data = cacheableService.findInstanceData(card);
				cache.clear();
				cache.put(card + cacheableService.getCacheKey(), data);
				log.debug("Cache updated for cache key: {} with Name: {}", cacheableService.getCacheKey(), card);
			}
		});
	}
	
	@SuppressWarnings("DataFlowIssue")
	public void refreshHeliosPerCountryCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
		CacheableService<?, ?> cacheableService = applicationContext.getBean(clazz);

		countries.forEach(country -> {
			Cache cache = cacheManager.getCache(country + cacheableService.getCacheName());
			if (Objects.nonNull(country + cacheableService.getCacheKey())) {
				Object data = cacheableService.findInstanceData(country);
				cache.clear();
				cache.put(country + cacheableService.getCacheKey(), data);
				log.debug("Cache updated for cache key: {} with Name: {}", country, cacheableService.getCacheKey());
			}
		});
	}

	public void refreshSmePerCountryCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
		CacheableService<?, ?> cacheableService = applicationContext.getBean(clazz);

		countries.forEach(country -> {
			Cache cache = cacheManager.getCache(country + cacheableService.getCacheName());
			if (Objects.nonNull(country + cacheableService.getCacheKey())) {
				Object data = cacheableService.findInstanceData(country);
				cache.clear();
				cache.put(country + cacheableService.getCacheKey(), data);
				log.debug("Cache updated for cache key: {} with Name: {}", country, cacheableService.getCacheKey());
			}
		});
	}
	
	@SuppressWarnings("DataFlowIssue")
	public void refreshOmnifyPerCompanyCaches(List<String> company, Class<? extends CacheableService<?, ?>> clazz) {
		CacheableService<?, ?> cacheableService = applicationContext.getBean(clazz);

		company.forEach(country -> {
			Cache cache = cacheManager.getCache(country + cacheableService.getCacheName());
			if (Objects.nonNull(country + cacheableService.getCacheKey())) {
				Object data = cacheableService.findInstanceData(country);
				cache.clear();
				cache.put(country + cacheableService.getCacheKey(), data);
				log.debug("Cache updated for cache key: {} with Name: {}", country, cacheableService.getCacheKey());
			}
		});
	}
	
	@SuppressWarnings("DataFlowIssue")
	public void refreshMdmCountriesCaches(List<String> countries, Class<? extends CacheableService<?, ?>> clazz) {
		CacheableService<?, ?> cacheableService = applicationContext.getBean(clazz);

		countries.forEach(country -> {
			Cache cache = cacheManager.getCache(country + cacheableService.getCacheName());
			if (Objects.nonNull(cacheableService.getCacheKey())) {
				Object data = cacheableService.findInstanceData(country);
				cache.clear();
				cache.put(cacheableService.getCacheKey(), data);
			} 
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
