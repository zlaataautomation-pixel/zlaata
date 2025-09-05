Feature: Home Page Banner upload verification admin panel
    
    
    @TC_UI_Zlaata_ADM_01
Scenario Outline: TC_UI_Zlaata_ADM_01 | Verify banner upload on home page section  | "<TD_ID>"  
  Given I upload an image "sample.jpg" in admin panel
  When I verify that the homepage first banner is "Home Page Banner"

Examples:  
  | TD_ID                  |  
  | TD_UI_Zlaata_ADM_01   |