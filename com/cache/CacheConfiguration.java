package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		// Base cache names
		List<String> baseCacheNames = Arrays.asList(
				HeliosConstants.EAB_CHARTS_CACHE_NAME,
				HeliosConstants.EAB_BARCHARTS_CACHE_NAME,
				SmeConstants.SME_SERVER_CACHE_NAME);

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
		
		allInstance.addAll(Arrays.asList("cardsData", "trxserver", "serversvitals", "packets","statusdialog"));
		allInstance.addAll(getCardWithPrefix("MdmChartsData", MdmConstants.COUNTRIES_LIST));

		return Stream.concat(baseCacheNames.stream(), allInstance.stream()).collect(Collectors.toList());
	}


    public static List<String> getCardWithPrefix(String name, List<String> list) {
        return list.stream().map(card -> card + name).collect(Collectors.toList());
    }

	@Bean
	public KeyGenerator keyGenerator() {
		return new TimeBasedKeyGenerator();
	}
}
