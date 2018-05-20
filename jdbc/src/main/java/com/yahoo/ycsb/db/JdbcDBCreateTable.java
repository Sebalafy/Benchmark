/**
 * Copyright (c) 2010 - 2016 Yahoo! Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying 
 * LICENSE file. 
 */
package com.yahoo.ycsb.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.HashMap;

/**
 * Utility class to create the table to be used by the benchmark.
 * 
 * @author sudipto
 */
public final class JdbcDBCreateTable {

  public static final String TABLE_TYPE_PROPERTY = "TPC_DS";
  public static final String TABLE_TYPE_PROPERTY_DEFAULT = "GENERAL";

  private static void usageMessage() {
    System.out.println("Create Table Client. Options:");
    System.out.println("  -p   key=value properties defined.");
    System.out.println("  -P   location of the properties file to load.");
    System.out.println("  -n   name of the table.");
    System.out.println("  -f   number of fields (default 10).");
    System.out.println("  -F   field of workload (TPC_DS).");
  }

  private static void createTable(Properties props, String tablename) throws SQLException {
    String driver = props.getProperty(JdbcDBClient.DRIVER_CLASS);
    String username = props.getProperty(JdbcDBClient.CONNECTION_USER);
    String password = props.getProperty(JdbcDBClient.CONNECTION_PASSWD, "");
    String url = props.getProperty(JdbcDBClient.CONNECTION_URL);
    int fieldcount = Integer.parseInt(props.getProperty(JdbcDBClient.FIELD_COUNT_PROPERTY,
        JdbcDBClient.FIELD_COUNT_PROPERTY_DEFAULT));

    if (driver == null || username == null || url == null) {
      throw new SQLException("Missing connection information.");
    }

    Connection conn = null;

    try {
      Class.forName(driver);

      conn = DriverManager.getConnection(url, username, password);
      Statement stmt = conn.createStatement();

      StringBuilder sql = new StringBuilder("DROP TABLE IF EXISTS ");
      sql.append(tablename);
      sql.append(";");

      stmt.execute(sql.toString());

      sql = new StringBuilder("CREATE TABLE ");
      sql.append(tablename);
      sql.append(" (YCSB_KEY VARCHAR PRIMARY KEY");

      for (int idx = 0; idx < fieldcount; idx++) {
        sql.append(", FIELD");
        sql.append(idx);
        sql.append(" TEXT");
      }
      sql.append(");");

      stmt.execute(sql.toString());

      System.out.println("Table " + tablename + " created..");
    } catch (ClassNotFoundException e) {
      throw new SQLException("JDBC Driver class not found.");
    } finally {
      if (conn != null) {
        System.out.println("Closing database connection.");
        conn.close();
      }
    }
  }

  //(YYB) Create TPC table
  private static void createTPCTable(Properties props) throws SQLException {
    String driver = props.getProperty(JdbcDBClient.DRIVER_CLASS);
    String username = props.getProperty(JdbcDBClient.CONNECTION_USER);
    String password = props.getProperty(JdbcDBClient.CONNECTION_PASSWD, "");
    String url = props.getProperty(JdbcDBClient.CONNECTION_URL);
    int fieldcount = Integer.parseInt(props.getProperty(JdbcDBClient.FIELD_COUNT_PROPERTY,
        JdbcDBClient.FIELD_COUNT_PROPERTY_DEFAULT));
    HashMap<String,String> tableAndField = new HashMap<String,String>();
    tableAndField.put("call_center", "("+
    " cc_call_center_id         char(16)          PRIMARY KEY ,"+
    " cc_rec_start_date         date                          ,"+
    " cc_rec_end_date           date                          ,"+
    " cc_closed_date_sk         integer                       ,"+
    " cc_open_date_sk           integer                       ,"+
    " cc_name                   varchar(50)                   ,"+
    " cc_class                  varchar(50)                   ,"+
    " cc_employees              integer                       ,"+
    " cc_sq_ft                  integer                       ,"+
    " cc_hours                  char(20)                      ,"+
    " cc_manager                varchar(40)                   ,"+
    " cc_mkt_id                 integer                       ,"+
    " cc_mkt_class              char(50)                      ,"+
    " cc_mkt_desc               varchar(100)                  ,"+
    " cc_market_manager         varchar(40)                   ,"+
    " cc_division               integer                       ,"+
    " cc_division_name          varchar(50)                   ,"+
    " cc_company                integer                       ,"+
    " cc_company_name           char(50)                      ,"+
    " cc_street_number          char(10)                      ,"+
    " cc_street_name            varchar(60)                   ,"+
    " cc_street_type            char(15)                      ,"+
    " cc_suite_number           char(10)                      ,"+
    " cc_city                   varchar(60)                   ,"+
    " cc_county                 varchar(30)                   ,"+
    " cc_state                  char(2)                       ,"+
    " cc_zip                    char(10)                      ,"+
    " cc_country                varchar(20)                   ,"+
    " cc_gmt_offset             decimal(5,2)                  ,"+
    " cc_tax_percentage         decimal(5,2)                  )");

    tableAndField.put("household_demographics","("+
    " hd_demo_sk                integer          PRIMARY KEY  ,"+
    " hd_income_band_sk         integer                       ,"+
    " hd_buy_potential          char(15)                      ,"+
    " hd_dep_count              integer                       ,"+
    " hd_vehicle_count          integer                       )");

    tableAndField.put("store_sales","("+
    " ss_sold_date_sk           integer                       ,"+
    " ss_sold_time_sk           integer                       ,"+
    " ss_item_sk                integer          PRIMARY KEY  ,"+
    " ss_customer_sk            integer                       ,"+
    " ss_cdemo_sk               integer                       ,"+
    " ss_hdemo_sk               integer                       ,"+
    " ss_addr_sk                integer                       ,"+
    " ss_store_sk               integer                       ,"+
    " ss_promo_sk               integer                       ,"+
    " ss_ticket_number          integer        PRIMARY KEY    ,"+
    " ss_quantity               integer                       ,"+
    " ss_wholesale_cost         decimal(7,2)                  ,"+
    " ss_list_price             decimal(7,2)                  ,"+
    " ss_sales_price            decimal(7,2)                  ,"+
    " ss_ext_discount_amt       decimal(7,2)                  ,"+
    " ss_ext_sales_price        decimal(7,2)                  ,"+
    " ss_ext_wholesale_cost     decimal(7,2)                  ,"+
    " ss_ext_list_price         decimal(7,2)                  ,"+
    " ss_ext_tax                decimal(7,2)                  ,"+
    " ss_coupon_amt             decimal(7,2)                  ,"+
    " ss_net_paid               decimal(7,2)                  ,"+
    " ss_net_paid_inc_tax       decimal(7,2)                  ,"+
    " ss_net_profit             decimal(7,2)                  )");

    tableAndField.put("catalog_page","("+
    " cp_catalog_page_sk        integer           PRIMARY KEY ,"+
    " cp_catalog_page_id        char(16)                      ,"+
    " cp_start_date_sk          integer                       ,"+
    " cp_end_date_sk            integer                       ,"+
    " cp_department             varchar(50)                   ,"+
    " cp_catalog_number         integer                       ,"+
    " cp_catalog_page_number    integer                       ,"+
    " cp_description            varchar(100)                  ,"+
    " cp_type                   varchar(100)                  )");

    tableAndField.put("income_band","("+
    " ib_income_band_sk         integer           PRIMARY KEY ,"+
    " ib_lower_bound            integer                       ,"+
    " ib_upper_bound            integer                       )");


    tableAndField.put("time_dim","("+
    " t_time_sk                 integer          PRIMARY KEY  ,"+
    " t_time_id                 char(16)                      ,"+
    " t_time                    integer                       ,"+
    " t_hour                    integer                       ,"+
    " t_minute                  integer                       ,"+
    " t_second                  integer                       ,"+
    " t_am_pm                   char(2)                       ,"+
    " t_shift                   char(20)                      ,"+
    " t_sub_shift               char(20)                      ,"+
    " t_meal_time               char(20)                      )");

    tableAndField.put("catalog_returns","("+
    " cr_returned_date_sk       integer                       ,"+
    " cr_returned_time_sk       integer                       ,"+
    " cr_item_sk                integer        PRIMARY KEY    ,"+
    " cr_refunded_customer_sk   integer                       ,"+
    " cr_refunded_cdemo_sk      integer                       ,"+
    " cr_refunded_hdemo_sk      integer                       ,"+
    " cr_refunded_addr_sk       integer                       ,"+
    " cr_returning_customer_sk  integer                       ,"+
    " cr_returning_cdemo_sk     integer                       ,"+
    " cr_returning_hdemo_sk     integer                       ,"+
    " cr_returning_addr_sk      integer                       ,"+
    " cr_call_center_sk         integer                       ,"+
    " cr_catalog_page_sk        integer                       ,"+
    " cr_ship_mode_sk           integer                       ,"+
    " cr_warehouse_sk           integer                       ,"+
    " cr_reason_sk              integer                       ,"+
    " cr_order_number           integer         PRIMARY KEY   ,"+
    " cr_return_quantity        integer                       ,"+
    " cr_return_amount          decimal(7,2)                  ,"+
    " cr_return_tax             decimal(7,2)                  ,"+
    " cr_return_amt_inc_tax     decimal(7,2)                  ,"+
    " cr_fee                    decimal(7,2)                  ,"+
    " cr_return_ship_cost       decimal(7,2)                  ,"+
    " cr_refunded_cash          decimal(7,2)                  ,"+
    " cr_reversed_charge        decimal(7,2)                  ,"+
    " cr_store_credit           decimal(7,2)                  ,"+
    " cr_net_loss               decimal(7,2)                  )");

    tableAndField.put("inventory","("+
    " inv_date_sk               integer           PRIMARY KEY ,"+
    " inv_item_sk               integer           PRIMARY KEY ,"+
    " inv_warehouse_sk          integer           PRIMARY KEY ,"+
    " inv_quantity_on_hand      integer                       )");


    tableAndField.put("warehouse","("+
    " w_warehouse_sk            integer          PRIMARY KEY  ,"+
    " w_warehouse_id            char(16)                      ,"+
    " w_warehouse_name          varchar(20)                   ,"+
    " w_warehouse_sq_ft         integer                       ,"+
    " w_street_number           char(10)                      ,"+
    " w_street_name             varchar(60)                   ,"+
    " w_street_type             char(15)                      ,"+
    " w_suite_number            char(10)                      ,"+
    " w_city                    varchar(60)                   ,"+
    " w_county                  varchar(30)                   ,"+
    " w_state                   char(2)                       ,"+
    " w_zip                     char(10)                      ,"+
    " w_country                 varchar(20)                   ,"+
    " w_gmt_offset              decimal(5,2)                  )");

    tableAndField.put("catalog_sales","("+
    " cs_sold_date_sk           integer                       ,"+
    " cs_sold_time_sk           integer                       ,"+
    " cs_ship_date_sk           integer                       ,"+
    " cs_bill_customer_sk       integer                       ,"+
    " cs_bill_cdemo_sk          integer                       ,"+
    " cs_bill_hdemo_sk          integer                       ,"+
    " cs_bill_addr_sk           integer                       ,"+
    " cs_ship_customer_sk       integer                       ,"+
    " cs_ship_cdemo_sk          integer                       ,"+
    " cs_ship_hdemo_sk          integer                       ,"+
    " cs_ship_addr_sk           integer                       ,"+
    " cs_call_center_sk         integer                       ,"+
    " cs_catalog_page_sk        integer                       ,"+
    " cs_ship_mode_sk           integer                       ,"+
    " cs_warehouse_sk           integer                       ,"+
    " cs_item_sk                integer         PRIMARY KEY   ,"+
    " cs_promo_sk               integer                       ,"+
    " cs_order_number           integer         PRIMARY KEY   ,"+
    " cs_quantity               integer                       ,"+
    " cs_wholesale_cost         decimal(7,2)                  ,"+
    " cs_list_price             decimal(7,2)                  ,"+
    " cs_sales_price            decimal(7,2)                  ,"+
    " cs_ext_discount_amt       decimal(7,2)                  ,"+
    " cs_ext_sales_price        decimal(7,2)                  ,"+
    " cs_ext_wholesale_cost     decimal(7,2)                  ,"+
    " cs_ext_list_price         decimal(7,2)                  ,"+
    " cs_ext_tax                decimal(7,2)                  ,"+
    " cs_coupon_amt             decimal(7,2)                  ,"+
    " cs_ext_ship_cost          decimal(7,2)                  ,"+
    " cs_net_paid               decimal(7,2)                  ,"+
    " cs_net_paid_inc_tax       decimal(7,2)                  ,"+
    " cs_net_paid_inc_ship      decimal(7,2)                  ,"+
    " cs_net_paid_inc_ship_tax  decimal(7,2)                  ,"+
    " cs_net_profit             decimal(7,2)                  )");

    tableAndField.put("item","("+
    " i_item_sk                 integer         PRIMARY KEY   ,"+
    " i_item_id                 char(16)                      ,"+
    " i_rec_start_date          date                          ,"+
    " i_rec_end_date            date                          ,"+
    " i_item_desc               varchar(200)                  ,"+
    " i_current_price           decimal(7,2)                  ,"+
    " i_wholesale_cost          decimal(7,2)                  ,"+
    " i_brand_id                integer                       ,"+
    " i_brand                   char(50)                      ,"+
    " i_class_id                integer                       ,"+
    " i_class                   char(50)                      ,"+
    " i_category_id             integer                       ,"+
    " i_category                char(50)                      ,"+
    " i_manufact_id             integer                       ,"+
    " i_manufact                char(50)                      ,"+
    " i_size                    char(20)                      ,"+
    " i_formulation             char(20)                      ,"+
    " i_color                   char(20)                      ,"+
    " i_units                   char(10)                      ,"+
    " i_container               char(10)                      ,"+
    " i_manager_id              integer                       ,"+
    " i_product_name            char(50)                      +");

    tableAndField.put("web_page","("+
    " wp_web_page_sk            integer       PRIMARY KEY     ,"+
    " wp_web_page_id            char(16)                      ,"+
    " wp_rec_start_date         date                          ,"+
    " wp_rec_end_date           date                          ,"+
    " wp_creation_date_sk       integer                       ,"+
    " wp_access_date_sk         integer                       ,"+
    " wp_autogen_flag           char(1)                       ,"+
    " wp_customer_sk            integer                       ,"+
    " wp_url                    varchar(100)                  ,"+
    " wp_type                   char(50)                      ,"+
    " wp_char_count             integer                       ,"+
    " wp_link_count             integer                       ,"+
    " wp_image_count            integer                       )");

    tableAndField.put("customer_address","("+
    " ca_address_sk             integer         PRIMARY KEY   ,"+
    " ca_address_id             char(16)                      ,"+
    " ca_street_number          char(10)                      ,"+
    " ca_street_name            varchar(60)                   ,"+
    " ca_street_type            char(15)                      ,"+
    " ca_suite_number           char(10)                      ,"+
    " ca_city                   varchar(60)                   ,"+
    " ca_county                 varchar(30)                   ,"+
    " ca_state                  char(2)                       ,"+
    " ca_zip                    char(10)                      ,"+
    " ca_country                varchar(20)                   ,"+
    " ca_gmt_offset             decimal(5,2)                  ,"+
    " ca_location_type          char(20)                      )");

    tableAndField.put("promotion","("+
    " p_promo_sk                integer          PRIMARY KEY  ,"+
    " p_promo_id                char(16)                      ,"+
    " p_start_date_sk           integer                       ,"+
    " p_end_date_sk             integer                       ,"+
    " p_item_sk                 integer                       ,"+
    " p_cost                    decimal(15,2)                 ,"+
    " p_response_target         integer                       ,"+
    " p_promo_name              char(50)                      ,"+
    " p_channel_dmail           char(1)                       ,"+
    " p_channel_email           char(1)                       ,"+
    " p_channel_catalog         char(1)                       ,"+
    " p_channel_tv              char(1)                       ,"+
    " p_channel_radio           char(1)                       ,"+
    " p_channel_press           char(1)                       ,"+
    " p_channel_event           char(1)                       ,"+
    " p_channel_demo            char(1)                       ,"+
    " p_channel_details         varchar(100)                  ,"+
    " p_purpose                 char(15)                      ,"+
    " p_discount_active         char(1)                       )");

    tableAndField.put("web_returns","("+
    " wr_returned_date_sk       integer                       ,"+
    " wr_returned_time_sk       integer                       ,"+
    " wr_item_sk                integer          PRIMARY KEY  ,"+
    " wr_refunded_customer_sk   integer                       ,"+
    " wr_refunded_cdemo_sk      integer                       ,"+
    " wr_refunded_hdemo_sk      integer                       ,"+
    " wr_refunded_addr_sk       integer                       ,"+
    " wr_returning_customer_sk  integer                       ,"+
    " wr_returning_cdemo_sk     integer                       ,"+
    " wr_returning_hdemo_sk     integer                       ,"+
    " wr_returning_addr_sk      integer                       ,"+
    " wr_web_page_sk            integer                       ,"+
    " wr_reason_sk              integer                       ,"+
    " wr_order_number           integer           PRIMARY KEY ,"+
    " wr_return_quantity        integer                       ,"+
    " wr_return_amt             decimal(7,2)                  ,"+
    " wr_return_tax             decimal(7,2)                  ,"+
    " wr_return_amt_inc_tax     decimal(7,2)                  ,"+
    " wr_fee                    decimal(7,2)                  ,"+
    " wr_return_ship_cost       decimal(7,2)                  ,"+
    " wr_refunded_cash          decimal(7,2)                  ,"+
    " wr_reversed_charge        decimal(7,2)                  ,"+
    " wr_account_credit         decimal(7,2)                  ,"+
    " wr_net_loss               decimal(7,2)                  )");

    tableAndField.put("customer","("+
    " ca_address_sk             integer           PRIMARY KEY ,"+
    " ca_address_id             char(16)                      ,"+
    " ca_street_number          char(10)                      ,"+
    " ca_street_name            varchar(60)                   ,"+
    " ca_street_type            char(15)                      ,"+
    " ca_suite_number           char(10)                      ,"+
    " ca_city                   varchar(60)                   ,"+
    " ca_county                 varchar(30)                   ,"+
    " ca_state                  char(2)                       ,"+
    " ca_zip                    char(10)                      ,"+
    " ca_country                varchar(20)                   ,"+
    " ca_gmt_offset             decimal(5,2)                  ,"+
    " ca_location_type          char(20)                      )");


    tableAndField.put("reason","("+
    " r_reason_sk               integer           PRIMARY KEY ,"+
    " r_reason_id               char(16)                      ,"+
    " r_reason_desc             char(100)                     )");

    tableAndField.put("web_sales","("+
    " ws_sold_date_sk           integer                       ,"+
    " ws_sold_time_sk           integer                       ,"+
    " ws_ship_date_sk           integer                       ,"+
    " ws_item_sk                integer         PRIMARY KEY   ,"+
    " ws_bill_customer_sk       integer                       ,"+
    " ws_bill_cdemo_sk          integer                       ,"+
    " ws_bill_hdemo_sk          integer                       ,"+
    " ws_bill_addr_sk           integer                       ,"+
    " ws_ship_customer_sk       integer                       ,"+
    " ws_ship_cdemo_sk          integer                       ,"+
    " ws_ship_hdemo_sk          integer                       ,"+
    " ws_ship_addr_sk           integer                       ,"+
    " ws_web_page_sk            integer                       ,"+
    " ws_web_site_sk            integer                       ,"+
    " ws_ship_mode_sk           integer                       ,"+
    " ws_warehouse_sk           integer                       ,"+
    " ws_promo_sk               integer                       ,"+
    " ws_order_number           integer         PRIMARY KEY   ,"+
    " ws_quantity               integer                       ,"+
    " ws_wholesale_cost         decimal(7,2)                  ,"+
    " ws_list_price             decimal(7,2)                  ,"+
    " ws_sales_price            decimal(7,2)                  ,"+
    " ws_ext_discount_amt       decimal(7,2)                  ,"+
    " ws_ext_sales_price        decimal(7,2)                  ,"+
    " ws_ext_wholesale_cost     decimal(7,2)                  ,"+
    " ws_ext_list_price         decimal(7,2)                  ,"+
    " ws_ext_tax                decimal(7,2)                  ,"+
    " ws_coupon_amt             decimal(7,2)                  ,"+
    " ws_ext_ship_cost          decimal(7,2)                  ,"+
    " ws_net_paid               decimal(7,2)                  ,"+
    " ws_net_paid_inc_tax       decimal(7,2)                  ,"+
    " ws_net_paid_inc_ship      decimal(7,2)                  ,"+
    " ws_net_paid_inc_ship_tax  decimal(7,2)                  ,"+
    " ws_net_profit             decimal(7,2)                  )");

    tableAndField.put("customer_demographics","("+
    " cd_demo_sk                integer          PRIMARY KEY  ,"+
    " cd_gender                 char(1)                       ,"+
    " cd_marital_status         char(1)                       ,"+
    " cd_education_status       char(20)                      ,"+
    " cd_purchase_estimate      integer                       ,"+
    " cd_credit_rating          char(10)                      ,"+
    " cd_dep_count              integer                       ,"+
    " cd_dep_employed_count     integer                       ,"+
    " cd_dep_college_count      integer                       )");

    tableAndField.put("ship_mode","("+
    " sm_ship_mode_sk           integer          PRIMARY KEY  ,"+
    " sm_ship_mode_id           char(16)                      ,"+
    " sm_type                   char(30)                      ,"+
    " sm_code                   char(10)                      ,"+
    " sm_carrier                char(20)                      ,"+
    " sm_contract               char(20)                      )");

    tableAndField.put("web_site","("+
    " web_site_sk               integer        PRIMARY KEY    ,"+
    " web_site_id               char(16)                      ,"+
    " web_rec_start_date        date                          ,"+
    " web_rec_end_date          date                          ,"+
    " web_name                  varchar(50)                   ,"+
    " web_open_date_sk          integer                       ,"+
    " web_close_date_sk         integer                       ,"+
    " web_class                 varchar(50)                   ,"+
    " web_manager               varchar(40)                   ,"+
    " web_mkt_id                integer                       ,"+
    " web_mkt_class             varchar(50)                   ,"+
    " web_mkt_desc              varchar(100)                  ,"+
    " web_market_manager        varchar(40)                   ,"+
    " web_company_id            integer                       ,"+
    " web_company_name          char(50)                      ,"+
    " web_street_number         char(10)                      ,"+
    " web_street_name           varchar(60)                   ,"+
    " web_street_type           char(15)                      ,"+
    " web_suite_number          char(10)                      ,"+
    " web_city                  varchar(60)                   ,"+
    " web_county                varchar(30)                   ,"+
    " web_state                 char(2)                       ,"+
    " web_zip                   char(10)                      ,"+
    " web_country               varchar(20)                   ,"+
    " web_gmt_offset            decimal(5,2)                  ,"+
    " web_tax_percentage        decimal(5,2)                  )");

    tableAndField.put("date_dim","("+
    " d_date_sk                 integer          PRIMARY KEY  ,"+
    " d_date_id                 char(16)                      ,"+
    " d_date                    date                          ,"+
    " d_month_seq               integer                       ,"+
    " d_week_seq                integer                       ,"+
    " d_quarter_seq             integer                       ,"+
    " d_year                    integer                       ,"+
    " d_dow                     integer                       ,"+
    " d_moy                     integer                       ,"+
    " d_dom                     integer                       ,"+
    " d_qoy                     integer                       ,"+
    " d_fy_year                 integer                       ,"+
    " d_fy_quarter_seq          integer                       ,"+
    " d_fy_week_seq             integer                       ,"+
    " d_day_name                char(9)                       ,"+
    " d_quarter_name            char(6)                       ,"+
    " d_holiday                 char(1)                       ,"+
    " d_weekend                 char(1)                       ,"+
    " d_following_holiday       char(1)                       ,"+
    " d_first_dom               integer                       ,"+
    " d_last_dom                integer                       ,"+
    " d_same_day_ly             integer                       ,"+
    " d_same_day_lq             integer                       ,"+
    " d_current_day             char(1)                       ,"+
    " d_current_week            char(1)                       ,"+
    " d_current_month           char(1)                       ,"+
    " d_current_quarter         char(1)                       ,"+
    " d_current_year            char(1)                       )");

    tableAndField.put("store","("+
    " s_store_sk                integer         PRIMARY KEY   ,"+
    " s_store_id                char(16)                      ,"+
    " s_rec_start_date          date                          ,"+
    " s_rec_end_date            date                          ,"+
    " s_closed_date_sk          integer                       ,"+
    " s_store_name              varchar(50)                   ,"+
    " s_number_employees        integer                       ,"+
    " s_floor_space             integer                       ,"+
    " s_hours                   char(20)                      ,"+
    " s_manager                 varchar(40)                   ,"+
    " s_market_id               integer                       ,"+
    " s_geography_class         varchar(100)                  ,"+
    " s_market_desc             varchar(100)                  ,"+
    " s_market_manager          varchar(40)                   ,"+
    " s_division_id             integer                       ,"+
    " s_division_name           varchar(50)                   ,"+
    " s_company_id              integer                       ,"+
    " s_company_name            varchar(50)                   ,"+
    " s_street_number           varchar(10)                   ,"+
    " s_street_name             varchar(60)                   ,"+
    " s_street_type             char(15)                      ,"+
    " s_suite_number            char(10)                      ,"+
    " s_city                    varchar(60)                   ,"+
    " s_county                  varchar(30)                   ,"+
    " s_state                   char(2)                       ,"+
    " s_zip                     char(10)                      ,"+
    " s_country                 varchar(20)                   ,"+
    " s_gmt_offset              decimal(5,2)                  ,"+
    " s_tax_precentage          decimal(5,2)                  )");

    tableAndField.put("store_returns","("+
    " sr_returned_date_sk       integer                       ,"+
    " sr_return_time_sk         integer                       ,"+
    " sr_item_sk                integer           PRIMARY KEY ,"+
    " sr_customer_sk            integer                       ,"+
    " sr_cdemo_sk               integer                       ,"+
    " sr_hdemo_sk               integer                       ,"+
    " sr_addr_sk                integer                       ,"+
    " sr_store_sk               integer                       ,"+
    " sr_reason_sk              integer                       ,"+
    " sr_ticket_number          integer           PRIMARY KEY ,"+
    " sr_return_quantity        integer                       ,"+
    " sr_return_amt             decimal(7,2)                  ,"+
    " sr_return_tax             decimal(7,2)                  ,"+
    " sr_return_amt_inc_tax     decimal(7,2)                  ,"+
    " sr_fee                    decimal(7,2)                  ,"+
    " sr_return_ship_cost       decimal(7,2)                  ,"+
    " sr_refunded_cash          decimal(7,2)                  ,"+
    " sr_reversed_charge        decimal(7,2)                  ,"+
    " sr_store_credit           decimal(7,2)                  ,"+
    " sr_net_loss               decimal(7,2)                  )");


    if (driver == null || username == null || url == null) {
      throw new SQLException("Missing connection information.");
    }

    Connection conn = null;

    try {
      Class.forName(driver);

      conn = DriverManager.getConnection(url, username, password);
      Statement stmt = conn.createStatement();
      String tablename;
      String fields;
      StringBuilder sql;
       for (Entry<String, String> entry : tableAndField.entrySet()) {
        tablename = entry.getKey();
        fields = entry.getValue();

        sql = new StringBuilder("DROP TABLE IF EXISTS ");
        sql.append(tablename);
        sql.append(";");
        stmt.execute(sql.toString());

        sql = new StringBuilder("CREATE TABLE ");
        sql.append(tablename);
        sql.append(" ");
        sql.append(fields);
        sql.append(";");
        stmt.execute(sql.toString());
        System.out.println("Table " + tablename + " created..");

    } catch (ClassNotFoundException e) {
      throw new SQLException("JDBC Driver class not found.");
    } finally {
      if (conn != null) {
        System.out.println("Closing database connection.");
        conn.close();
      }
    }
  }


  /**
   * @param args
   */
  public static void main(String[] args) {

    if (args.length == 0) {
      usageMessage();
      System.exit(0);
    }

    String tablename = null;
    int fieldcount = -1;
    Properties props = new Properties();
    Properties fileprops = new Properties();

    // parse arguments
    int argindex = 0;
    while (args[argindex].startsWith("-")) {
      if (args[argindex].compareTo("-P") == 0) {
        argindex++;
        if (argindex >= args.length) {
          usageMessage();
          System.exit(0);
        }
        String propfile = args[argindex];
        argindex++;

        Properties myfileprops = new Properties();
        try {
          myfileprops.load(new FileInputStream(propfile));
        } catch (IOException e) {
          System.out.println(e.getMessage());
          System.exit(0);
        }

        // Issue #5 - remove call to stringPropertyNames to make compilable
        // under Java 1.5
        for (Enumeration<?> e = myfileprops.propertyNames(); e.hasMoreElements();) {
          String prop = (String) e.nextElement();

          fileprops.setProperty(prop, myfileprops.getProperty(prop));
        }
      // (YYB)
      } else if ((args[argindex].compareTo("-F") == 0){ 
        argindex++;
        if (args[argindex].compareTo("TPC_DS") == 0) {
          props.setProperty(TABLE_TYPE_PROPERTY, args[argindex]);
        }
        argindex++;
      } else if (args[argindex].compareTo("-p") == 0) {
        argindex++;
        if (argindex >= args.length) {
          usageMessage();
          System.exit(0);
        }
        int eq = args[argindex].indexOf('=');
        if (eq < 0) {
          usageMessage();
          System.exit(0);
        }

        String name = args[argindex].substring(0, eq);
        String value = args[argindex].substring(eq + 1);
        props.put(name, value);
        argindex++;
      } else if (args[argindex].compareTo("-n") == 0) {
        argindex++;
        if (argindex >= args.length) {
          usageMessage();
          System.exit(0);
        }
        tablename = args[argindex++];
      } else if (args[argindex].compareTo("-f") == 0) {
        argindex++;
        if (argindex >= args.length) {
          usageMessage();
          System.exit(0);
        }
        try {
          fieldcount = Integer.parseInt(args[argindex++]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid number for field count");
          usageMessage();
          System.exit(1);
        }
      } else {
        System.out.println("Unknown option " + args[argindex]);
        usageMessage();
        System.exit(0);
      }

      if (argindex >= args.length) {
        break;
      }
    }

    if (argindex != args.length) {
      usageMessage();
      System.exit(0);
    }

    // overwrite file properties with properties from the command line

    // Issue #5 - remove call to stringPropertyNames to make compilable under
    // Java 1.5
    for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
      String prop = (String) e.nextElement();

      fileprops.setProperty(prop, props.getProperty(prop));
    }

    props = fileprops;

    if (tablename == null) {
      System.err.println("table name missing.");
      usageMessage();
      System.exit(1);
    }

    if (fieldcount > 0) {
      props.setProperty(JdbcDBClient.FIELD_COUNT_PROPERTY, String.valueOf(fieldcount));
    }

    try {
      if (props.getProperty(TABLE_TYPE_PROPERTY,TABLE_TYPE_PROPERTY_DEFAULT).equals("TPC_DS")) {
        //创建24个表！！！！！
        createTPCTable(props);
      } else {
        createTable(props, tablename);
      }
      
    } catch (SQLException e) {
      System.err.println("Error in creating table. " + e);
      System.exit(1);
    }
  }

  /**
   * Hidden constructor.
   */
  private JdbcDBCreateTable() {
    super();
  }
}
