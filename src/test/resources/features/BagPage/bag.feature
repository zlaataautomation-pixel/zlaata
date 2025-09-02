Feature: Bag Page Functionality

  #===========================================================================
  # Test Case ID :: TC_UI_Zlaata_BP_01
  #===========================================================================
  # Scenario Description: Complete Bag Page
  # Expected: Bag page sanity
  #===========================================================================
  
  @TC_UI_Zlaata_BP_01
  Scenario Outline: TC_UI_Zlaata_BP_01 |Verify Bag Item Count Display|"<TD_ID>"
    Given User Verifies Bag Item Count
     

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_01     |

  @TC_UI_Zlaata_BP_02
  Scenario Outline: TC_UI_Zlaata_BP_02 |Verify Display of Wishlist Button|"<TD_ID>"
    Given User Verifies Display of Wishlist Button

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_02     |

  @TC_UI_Zlaata_BP_03
  Scenario Outline: TC_UI_Zlaata_BP_03 |Verify Display of Delete Button|"<TD_ID>"
    Given User Verifies Display of Delete Button

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_03     |

  @TC_UI_Zlaata_BP_04
  Scenario Outline: TC_UI_Zlaata_BP_04 |Verify That User Can Change Product Size|"<TD_ID>"
    Given User Verifies That User Can Change Product Size

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_04     |

  @TC_UI_Zlaata_BP_05
  Scenario Outline: TC_UI_Zlaata_BP_05 |Verify User Can Increase or Decrease Product Quantity|"<TD_ID>"
    Given User Verifies User Can Increase or Decrease Product Quantity

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_05     |

  @TC_UI_Zlaata_BP_06
  Scenario Outline: TC_UI_Zlaata_BP_06 |Verify User Can Add New Product|"<TD_ID>"
    Given User Verifies User Can Add New Product

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_06     |

  @TC_UI_Zlaata_BP_07
  Scenario Outline: TC_UI_Zlaata_BP_07 |Verify That Adding Product to Bag Count is Displaying or Not Above Bag Icon|"<TD_ID>"
    Given User Verifies That Adding Product to Bag Count is Displaying or Not Above Bag Icon

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_07     |

  @TC_UI_Zlaata_BP_08
  Scenario Outline: TC_UI_Zlaata_BP_08 |Verify That Adding New Product or Deleting Product Count Increases or Decreases|"<TD_ID>"
    Given User Verifies That Adding New Product or Deleting Product Count Increases or Decreases

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_08     |

