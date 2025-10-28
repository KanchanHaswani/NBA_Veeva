#!/bin/bash

# NBA Automation Framework - Simple Driver Copy Script
# This script copies your downloaded drivers to the project drivers directory

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}NBA Automation Framework - Driver Copy${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# Create drivers directory
DRIVERS_DIR="./drivers"
echo -e "${YELLOW}Creating drivers directory: $DRIVERS_DIR${NC}"
mkdir -p "$DRIVERS_DIR"

echo -e "${BLUE}Please provide the paths to your downloaded drivers:${NC}"
echo ""

# Get ChromeDriver path
read -p "ChromeDriver path: " chrome_path
if [[ -n "$chrome_path" && -f "$chrome_path" ]]; then
    cp "$chrome_path" "$DRIVERS_DIR/chromedriver"
    chmod +x "$DRIVERS_DIR/chromedriver"
    echo -e "${GREEN}✅ ChromeDriver copied to $DRIVERS_DIR/chromedriver${NC}"
else
    echo -e "${RED}❌ ChromeDriver not found at: $chrome_path${NC}"
fi

echo ""

# Get GeckoDriver path (optional)
read -p "GeckoDriver path (optional, press Enter to skip): " firefox_path
if [[ -n "$firefox_path" && -f "$firefox_path" ]]; then
    cp "$firefox_path" "$DRIVERS_DIR/geckodriver"
    chmod +x "$DRIVERS_DIR/geckodriver"
    echo -e "${GREEN}✅ GeckoDriver copied to $DRIVERS_DIR/geckodriver${NC}"
fi

echo ""

# Get EdgeDriver path (optional)
read -p "EdgeDriver path (optional, press Enter to skip): " edge_path
if [[ -n "$edge_path" && -f "$edge_path" ]]; then
    cp "$edge_path" "$DRIVERS_DIR/msedgedriver"
    chmod +x "$DRIVERS_DIR/msedgedriver"
    echo -e "${GREEN}✅ EdgeDriver copied to $DRIVERS_DIR/msedgedriver${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Driver copy completed!${NC}"
echo ""
echo -e "${BLUE}Your drivers are now in:${NC}"
ls -la "$DRIVERS_DIR"
echo ""
echo -e "${BLUE}Configuration is already set to use project drivers:${NC}"
echo "- webdriver.chrome.path=./drivers/chromedriver"
echo "- webdriver.firefox.path=./drivers/geckodriver"
echo "- webdriver.edge.path=./drivers/msedgedriver"
echo "- webdriver.offline.mode=true"
echo "- webdriver.auto.download=false"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo "1. Test the setup: mvn test -Dtest=FrameworkDemoTest"
echo "2. Run Cucumber tests: mvn test -Dtest=WarriorsTestRunner"
echo ""
echo -e "${GREEN}The framework will now always use your drivers from ./drivers/ directory!${NC}"
