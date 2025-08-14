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

  @TC_UI_Zlaata_BP_09
  Scenario Outline: TC_UI_Zlaata_BP_09 |Verify "You’ll Earn" Line Display|"<TD_ID>"
    Given User Verifies "You’ll Earn" Line Display

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_09     |

  @TC_UI_Zlaata_BP_10
  Scenario Outline: TC_UI_Zlaata_BP_10 |Verify "You’ll Earn thread amount " Calculation Based on Discount Amount|"<TD_ID>"
    Given User Verifies "You’ll Earn" Thread Amount Calculation Based on Discount Amount

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_10     |

  @TC_UI_Zlaata_BP_11
  Scenario Outline: TC_UI_Zlaata_BP_11 |Verify Increasing/Decreasing/Adding Product's "You’ll Earn" Thread Amount Based on Change|"<TD_ID>"
    Given User Verifies Increasing/Decreasing/Adding Product's "You’ll Earn" Thread Amount Based on Change

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_11     |

  @TC_UI_Zlaata_BP_12
  Scenario Outline: TC_UI_Zlaata_BP_12 |Verify "You Saved" and Total Amount Line Display|"<TD_ID>"
    Given User Verifies "You Saved" and Total Amount Line Display

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_12     |

  @TC_UI_Zlaata_BP_13
  Scenario Outline: TC_UI_Zlaata_BP_13 |Verify "You Saved" Amount Calculation Based on Original Price Minus Discount Amount|"<TD_ID>"
    Given User Verifies "You Saved" Amount Calculation Based on Original Price Minus Discount Amount

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_13     |

  @TC_UI_Zlaata_BP_14
  Scenario Outline: TC_UI_Zlaata_BP_14 |Verify "Total Amount" Calculating Based on Discounted Amount|"<TD_ID>"
    Given User Verifies "Total Amount" Calculating Based on Discounted Amount

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_14     |

  @TC_UI_Zlaata_BP_15
  Scenario Outline: TC_UI_Zlaata_BP_15 |Verify Increasing/Decreasing/Adding Product's "You Saved" & Total Amount Based on Change|"<TD_ID>"
    Given User Verifies Increasing/Decreasing/Adding Product's "You Saved" & Total Amount Based on Change

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_15     |

  @TC_UI_Zlaata_BP_16
  Scenario Outline: TC_UI_Zlaata_BP_16 |Verify "Buy Now" Button Functionality|"<TD_ID>"
    Given User Verifies "Buy Now" Button Functionality

    Examples:
      | TD_ID                  |
      | TD_UI_Zlaata_BP_16     |
