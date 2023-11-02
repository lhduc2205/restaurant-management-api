package com.lhduc.restaurantmanagementapi.model.dto.request.sort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SortRequest {
    private static final String SORT_FIELD_SEPARATOR = ",";
    private static final String DESCENDING_SIGN = "-";

    List<SortableField> sortableFields = new ArrayList<>();

    public void parseSortString(String sortString) {
        String[] sortFields = sortString.split(SORT_FIELD_SEPARATOR);

        for (String sortField : sortFields) {
            if (sortField.isBlank()) {
                continue;
            }

            SortableField sortableField = new SortableField();

            if (sortField.startsWith(DESCENDING_SIGN)) {
                sortableField.setSortDirection(Sort.Direction.DESC);
                sortableField.setFieldName(sortField.split(DESCENDING_SIGN)[1]);
            } else {
                sortableField.setSortDirection(Sort.Direction.ASC);
                sortableField.setFieldName(normalizeFieldName(sortField));
            }

            sortableFields.add(sortableField);
        }
    }

    public List<Sort.Order> buildSortOrders() {
        List<Sort.Order> sortOrders = new ArrayList<>();

        for (SortableField sortableField : sortableFields) {
            sortOrders.add(new Sort.Order(sortableField.getSortDirection(), sortableField.getFieldName()));
        }

        return sortOrders;
    }

    private String normalizeFieldName(String field) {
        if (!Character.isLetterOrDigit(field.charAt(0))) {
            return field.substring(1);
        }

        return field;
    }
}
