package gini.ginidashboardservice.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gini.ginidashboardservice.dto.ProductsDTO;
import gini.ginidashboardservice.models.SalesAgent;
import gini.ginidashboardservice.repositories.SalesAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SalesAgentController {

    @Autowired
    SalesAgentRepository salesAgentRepository;

    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics(@PathVariable Long id) {
        Optional<SalesAgent> salesAgent = salesAgentRepository.findById(id);
        if (salesAgent.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("performance_segment", salesAgent.get().getPerformanceSegment());
            response.put("lifetime_value", salesAgent.get().getLifetimeValue());
            response.put("engagement_score", salesAgent.get().getEngagementScore());
            response.put("sentiment_60_days", salesAgent.get().getSentiment60Day());
            response.put("sales_forecast_30_days", salesAgent.get().getSalesForecast30Day());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/strengths")
    public ResponseEntity<String[]> getStrengths(@PathVariable Long id) {
        Optional<SalesAgent> salesAgent = salesAgentRepository.findById(id);
        if (salesAgent.isPresent()) {
            String strengths = salesAgent.get().getStrengths();
            String[] strengthsArray = strengths.split(", ");
            return ResponseEntity.ok(strengthsArray);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductsDTO>> getProducts(@PathVariable Long id) {
        Optional<SalesAgent> salesAgent = salesAgentRepository.findById(id);
        if (salesAgent.isPresent()) {
            String productsJson = salesAgent.get().getProducts();
            ObjectMapper mapper = new ObjectMapper();
            try {
                // Parse JSON into a list of maps to handle dynamic keys
                List<Map<String, String>> productsMapList = mapper.readValue(productsJson, new TypeReference<List<Map<String, String>>>() {});

                // Convert the list of maps into a list of ProductsDTO
                List<ProductsDTO> productsList = productsMapList.stream()
                        .map(map -> map.entrySet().stream()
                                .map(entry -> new ProductsDTO(entry.getKey(), entry.getValue()))
                                .findFirst()
                                .orElse(null))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(productsList);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }    }

    @GetMapping("/{id}/topics")
    public ResponseEntity<String[]> getTopics(@PathVariable Long id) {
        Optional<SalesAgent> salesAgent = salesAgentRepository.findById(id);
        if (salesAgent.isPresent()) {
            String topics = salesAgent.get().getTopics();
            String[] topicsArray = topics.split(", ");
            return ResponseEntity.ok(topicsArray);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
