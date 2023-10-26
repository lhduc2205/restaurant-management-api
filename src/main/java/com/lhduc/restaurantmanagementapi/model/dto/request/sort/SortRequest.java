package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SortRequest {
    private String sort = "";

    private static final String FIELD_SEPARATOR = ",";
    private static final String DESC_SIGN = "-";

    List<SortItem> sortItems = new ArrayList<>();

    private void splitSortFields() {
        cleanupSortField();
        String[] sortFields = sort.split(FIELD_SEPARATOR);

        for (String sortField : sortFields) {
            if (sortField.isBlank()) {
                continue;
            }

            SortItem sortItem = new SortItem();

            if (sortField.startsWith(DESC_SIGN)) {
                sortItem.setSortType(Sort.Direction.DESC);
                sortItem.setSortField(sortField.split(DESC_SIGN)[1]);
            } else {
                sortItem.setSortType(Sort.Direction.ASC);
                sortItem.setSortField(removeWeirdChar(sortField));
            }

            sortItems.add(sortItem);
        }
    }

    public List<Sort.Order> extractSortOrder() {
        splitSortFields();
        List<Sort.Order> sortOrders = new ArrayList<>();

        for (SortItem sortItem : sortItems) {
            sortOrders.add(new Sort.Order(sortItem.getSortType(), sortItem.getSortField()));
        }

        return sortOrders;
    }

    private void cleanupSortField() {
        sort = sort.replace(" ", "");
    }

    private String removeWeirdChar(String field) {
        if (!Character.isLetterOrDigit(field.charAt(0))) {
            return field.substring(1);
        }

        return field;
    }
}
