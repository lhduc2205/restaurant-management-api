package com.lhduc.restaurantmanagementapi.common.constant;

/**
 * This class contains constants for URIs or endpoints used in the application.
 */
public class UriConstant {
    private UriConstant() {}
    public static final String API_PREFIX = "/api";
    public static final String API_VERSION_1 = API_PREFIX + "/v1";
    public static final String MENU_ITEMS_ENDPOINT = API_VERSION_1 + "/menu-items";
    public static final String BILLS_ENDPOINT = API_VERSION_1 + "/bills";
}
