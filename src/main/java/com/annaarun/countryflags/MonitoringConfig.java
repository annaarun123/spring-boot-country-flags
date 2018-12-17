package com.annaarun.countryflags;

import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.spring.boot.SpringBootMetricsCollector;
import io.prometheus.client.Counter;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
class MonitoringConfig {

    // Counter for requests that try to get all countries
    public static final Counter allCountriesRequests = Counter.build()
        .name("country_flags_all_countries_requests_total").help("All Countries Total requests.").register();

    // Counter for requests that try to filter by continent
    public static final Counter continentRequests = Counter.build()
        .name("country_flags_by_continent_requests_total").help("By Continent Total requests.").labelNames("continent").register();

    // Counter for requests that try to filter by country
    public static final Counter countryRequests = Counter.build()
        .name("country_flags_by_country_requests_total").help("By Country Total requests.").labelNames("country").register();

    @Bean
    SpringBootMetricsCollector springBootMetricsCollector(Collection<PublicMetrics> publicMetrics) {

        SpringBootMetricsCollector springBootMetricsCollector = new SpringBootMetricsCollector(publicMetrics);
        springBootMetricsCollector.register();

        return springBootMetricsCollector;
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        DefaultExports.initialize();
        return new ServletRegistrationBean(new MetricsServlet(), "/metrics");
    }
}
