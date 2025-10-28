#!/bin/bash

# NBA Automation Framework - Quick Driver Setup
# This script helps you quickly set up your existing drivers

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}NBA Automation Framework - Quick Driver Setup${NC}"
echo -e "${BLUE}============================================${NC}"
echo ""

# Create drivers directory
DRIVERS_DIR="./drivers"
echo -e "${YELLOW}Creating drivers directory...${NC}"
mkdir -p "$DRIVERS_DIR"

echo -e "${BLUE}Choose your setup option:${NC}"
echo "1) Copy drivers to project directory (Recommended)"
echo "2) Configure specific driver paths"
echo "3) Use system PATH drivers"
echo "4) Show current driver detection"

read -p "Enter your choice (1-4): " choice

case $choice in
    1)
        echo -e "${YELLOW}Option 1: Project Directory Setup${NC}"
        echo ""
        echo "Please provide the paths to your drivers:"
        
        read -p "ChromeDriver path: " chrome_path
        read -p "GeckoDriver path (optional): " firefox_path
        read -p "EdgeDriver path (optional): " edge_path
        
        # Copy ChromeDriver
        if [[ -n "$chrome_path" && -f "$chrome_path" ]]; then
            cp "$chrome_path" "$DRIVERS_DIR/chromedriver"
            chmod +x "$DRIVERS_DIR/chromedriver"
            echo -e "${GREEN}✅ ChromeDriver copied to $DRIVERS_DIR/chromedriver${NC}"
        else
            echo -e "${RED}❌ ChromeDriver not found at: $chrome_path${NC}"
        fi
        
        # Copy GeckoDriver
        if [[ -n "$firefox_path" && -f "$firefox_path" ]]; then
            cp "$firefox_path" "$DRIVERS_DIR/geckodriver"
            chmod +x "$DRIVERS_DIR/geckodriver"
            echo -e "${GREEN}✅ GeckoDriver copied to $DRIVERS_DIR/geckodriver${NC}"
        fi
        
        # Copy EdgeDriver
        if [[ -n "$edge_path" && -f "$edge_path" ]]; then
            cp "$edge_path" "$DRIVERS_DIR/msedgedriver"
            chmod +x "$DRIVERS_DIR/msedgedriver"
            echo -e "${GREEN}✅ EdgeDriver copied to $DRIVERS_DIR/msedgedriver${NC}"
        fi
        
        # Update configuration
        CONFIG_FILE="./automation-framework/src/main/resources/config.properties"
        if [[ -f "$CONFIG_FILE" ]]; then
            echo -e "${YELLOW}Updating configuration...${NC}"
            
            # Backup original config
            cp "$CONFIG_FILE" "$CONFIG_FILE.backup"
            
            # Update driver paths
            sed -i.bak "s|webdriver.chrome.path=|webdriver.chrome.path=./drivers/chromedriver|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.firefox.path=|webdriver.firefox.path=./drivers/geckodriver|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.edge.path=|webdriver.edge.path=./drivers/msedgedriver|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.offline.mode=false|webdriver.offline.mode=true|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.auto.download=true|webdriver.auto.download=false|" "$CONFIG_FILE"
            
            echo -e "${GREEN}✅ Configuration updated${NC}"
        else
            echo -e "${RED}❌ Configuration file not found: $CONFIG_FILE${NC}"
        fi
        ;;
        
    2)
        echo -e "${YELLOW}Option 2: Specific Path Configuration${NC}"
        echo ""
        echo "Please provide the full paths to your drivers:"
        
        read -p "ChromeDriver full path: " chrome_path
        read -p "GeckoDriver full path (optional): " firefox_path
        read -p "EdgeDriver full path (optional): " edge_path
        
        # Update configuration
        CONFIG_FILE="./automation-framework/src/main/resources/config.properties"
        if [[ -f "$CONFIG_FILE" ]]; then
            echo -e "${YELLOW}Updating configuration...${NC}"
            
            # Backup original config
            cp "$CONFIG_FILE" "$CONFIG_FILE.backup"
            
            # Update driver paths
            if [[ -n "$chrome_path" ]]; then
                sed -i.bak "s|webdriver.chrome.path=|webdriver.chrome.path=$chrome_path|" "$CONFIG_FILE"
            fi
            if [[ -n "$firefox_path" ]]; then
                sed -i.bak "s|webdriver.firefox.path=|webdriver.firefox.path=$firefox_path|" "$CONFIG_FILE"
            fi
            if [[ -n "$edge_path" ]]; then
                sed -i.bak "s|webdriver.edge.path=|webdriver.edge.path=$edge_path|" "$CONFIG_FILE"
            fi
            sed -i.bak "s|webdriver.offline.mode=false|webdriver.offline.mode=true|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.auto.download=true|webdriver.auto.download=false|" "$CONFIG_FILE"
            
            echo -e "${GREEN}✅ Configuration updated${NC}"
        else
            echo -e "${RED}❌ Configuration file not found: $CONFIG_FILE${NC}"
        fi
        ;;
        
    3)
        echo -e "${YELLOW}Option 3: System PATH Configuration${NC}"
        echo ""
        echo "Make sure your drivers are in system PATH:"
        echo "- /usr/local/bin/"
        echo "- /usr/bin/"
        echo "- ~/.local/bin/"
        echo "- ~/bin/"
        echo ""
        
        # Check if drivers are in PATH
        if command -v chromedriver &> /dev/null; then
            echo -e "${GREEN}✅ ChromeDriver found in PATH: $(which chromedriver)${NC}"
        else
            echo -e "${RED}❌ ChromeDriver not found in PATH${NC}"
        fi
        
        if command -v geckodriver &> /dev/null; then
            echo -e "${GREEN}✅ GeckoDriver found in PATH: $(which geckodriver)${NC}"
        else
            echo -e "${YELLOW}⚠️  GeckoDriver not found in PATH${NC}"
        fi
        
        if command -v msedgedriver &> /dev/null; then
            echo -e "${GREEN}✅ EdgeDriver found in PATH: $(which msedgedriver)${NC}"
        else
            echo -e "${YELLOW}⚠️  EdgeDriver not found in PATH${NC}"
        fi
        
        # Update configuration
        CONFIG_FILE="./automation-framework/src/main/resources/config.properties"
        if [[ -f "$CONFIG_FILE" ]]; then
            echo -e "${YELLOW}Updating configuration...${NC}"
            
            # Backup original config
            cp "$CONFIG_FILE" "$CONFIG_FILE.backup"
            
            # Clear driver paths and enable offline mode
            sed -i.bak "s|webdriver.chrome.path=.*|webdriver.chrome.path=|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.firefox.path=.*|webdriver.firefox.path=|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.edge.path=.*|webdriver.edge.path=|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.offline.mode=false|webdriver.offline.mode=true|" "$CONFIG_FILE"
            sed -i.bak "s|webdriver.auto.download=true|webdriver.auto.download=false|" "$CONFIG_FILE"
            
            echo -e "${GREEN}✅ Configuration updated${NC}"
        else
            echo -e "${RED}❌ Configuration file not found: $CONFIG_FILE${NC}"
        fi
        ;;
        
    4)
        echo -e "${YELLOW}Current Driver Detection${NC}"
        echo ""
        echo "Checking common driver locations..."
        
        # Check project drivers directory
        if [[ -f "./drivers/chromedriver" ]]; then
            echo -e "${GREEN}✅ ChromeDriver found: ./drivers/chromedriver${NC}"
        else
            echo -e "${RED}❌ ChromeDriver not found: ./drivers/chromedriver${NC}"
        fi
        
        # Check system PATH
        if command -v chromedriver &> /dev/null; then
            echo -e "${GREEN}✅ ChromeDriver found in PATH: $(which chromedriver)${NC}"
        else
            echo -e "${RED}❌ ChromeDriver not found in PATH${NC}"
        fi
        
        # Check common locations
        COMMON_PATHS=("/usr/local/bin/chromedriver" "/usr/bin/chromedriver" "/opt/homebrew/bin/chromedriver" "$HOME/.local/bin/chromedriver")
        for path in "${COMMON_PATHS[@]}"; do
            if [[ -f "$path" ]]; then
                echo -e "${GREEN}✅ ChromeDriver found: $path${NC}"
            fi
        done
        ;;
        
    *)
        echo -e "${RED}Invalid choice. Exiting.${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}🎉 Driver setup completed!${NC}"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo "1. Test the setup: mvn test -Dtest=FrameworkDemoTest"
echo "2. Run Cucumber tests: mvn test -Dtest=WarriorsTestRunner"
echo "3. Check logs for driver initialization strategy"
echo ""
echo -e "${BLUE}Driver locations checked by framework:${NC}"
echo "- ./drivers/"
echo "- ./test-drivers/"
echo "- /usr/local/bin/"
echo "- /usr/bin/"
echo "- /opt/homebrew/bin/"
echo "- ~/.local/bin/"
echo "- ~/bin/"
echo "- /tmp/drivers/"
echo ""
echo -e "${BLUE}Configuration file:${NC}"
echo "./automation-framework/src/main/resources/config.properties"
