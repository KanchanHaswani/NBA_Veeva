#!/bin/bash

# NBA Automation Framework - Driver Setup Script
# This script helps install WebDriver executables for offline testing

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DRIVERS_DIR="./drivers"
CACHE_DIR="$HOME/.cache/selenium-drivers"
CHROME_VERSION="119.0.6045.105"
FIREFOX_VERSION="0.33.0"
EDGE_VERSION="119.0.2151.58"

# Detect OS
OS=""
ARCH=""
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="linux"
    ARCH="linux64"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    OS="mac"
    ARCH="mac64"
elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
    OS="win"
    ARCH="win64"
else
    echo -e "${RED}Unsupported operating system: $OSTYPE${NC}"
    exit 1
fi

echo -e "${BLUE}NBA Automation Framework - Driver Setup${NC}"
echo -e "${BLUE}=====================================${NC}"
echo -e "OS: $OS"
echo -e "Architecture: $ARCH"
echo ""

# Function to download driver
download_driver() {
    local driver_name=$1
    local version=$2
    local url=$3
    local filename=$4
    
    echo -e "${YELLOW}Downloading $driver_name...${NC}"
    
    # Create drivers directory
    mkdir -p "$DRIVERS_DIR"
    
    # Download driver
    if command -v curl &> /dev/null; then
        curl -L -o "$DRIVERS_DIR/$filename" "$url"
    elif command -v wget &> /dev/null; then
        wget -O "$DRIVERS_DIR/$filename" "$url"
    else
        echo -e "${RED}Neither curl nor wget found. Please install one of them.${NC}"
        return 1
    fi
    
    # Extract if it's a zip file
    if [[ "$filename" == *.zip ]]; then
        echo -e "${YELLOW}Extracting $filename...${NC}"
        unzip -o "$DRIVERS_DIR/$filename" -d "$DRIVERS_DIR"
        rm "$DRIVERS_DIR/$filename"
    fi
    
    # Make executable
    chmod +x "$DRIVERS_DIR"/*
    
    echo -e "${GREEN}✅ $driver_name downloaded successfully${NC}"
}

# Function to setup ChromeDriver
setup_chromedriver() {
    echo -e "${BLUE}Setting up ChromeDriver...${NC}"
    
    local filename="chromedriver_$ARCH.zip"
    local url="https://chromedriver.storage.googleapis.com/$CHROME_VERSION/$filename"
    
    download_driver "ChromeDriver" "$CHROME_VERSION" "$url" "$filename"
    
    # Find the actual chromedriver executable
    local chromedriver_path=$(find "$DRIVERS_DIR" -name "chromedriver*" -type f | head -1)
    if [[ -n "$chromedriver_path" ]]; then
        echo -e "${GREEN}ChromeDriver installed at: $chromedriver_path${NC}"
        
        # Add to PATH suggestion
        echo -e "${YELLOW}To use this driver, add to your shell profile:${NC}"
        echo -e "export PATH=\"$(dirname "$chromedriver_path"):\$PATH\""
    fi
}

# Function to setup GeckoDriver
setup_geckodriver() {
    echo -e "${BLUE}Setting up GeckoDriver (Firefox)...${NC}"
    
    local filename="geckodriver-v$FIREFOX_VERSION-$ARCH.tar.gz"
    local url="https://github.com/mozilla/geckodriver/releases/download/v$FIREFOX_VERSION/$filename"
    
    download_driver "GeckoDriver" "$FIREFOX_VERSION" "$url" "$filename"
    
    # Find the actual geckodriver executable
    local geckodriver_path=$(find "$DRIVERS_DIR" -name "geckodriver*" -type f | head -1)
    if [[ -n "$geckodriver_path" ]]; then
        echo -e "${GREEN}GeckoDriver installed at: $geckodriver_path${NC}"
        
        # Add to PATH suggestion
        echo -e "${YELLOW}To use this driver, add to your shell profile:${NC}"
        echo -e "export PATH=\"$(dirname "$geckodriver_path"):\$PATH\""
    fi
}

# Function to setup EdgeDriver
setup_edgedriver() {
    echo -e "${BLUE}Setting up EdgeDriver...${NC}"
    
    local filename="edgedriver_$ARCH.zip"
    local url="https://msedgedriver.azureedge.net/$EDGE_VERSION/$filename"
    
    download_driver "EdgeDriver" "$EDGE_VERSION" "$url" "$filename"
    
    # Find the actual msedgedriver executable
    local edgedriver_path=$(find "$DRIVERS_DIR" -name "msedgedriver*" -type f | head -1)
    if [[ -n "$edgedriver_path" ]]; then
        echo -e "${GREEN}EdgeDriver installed at: $edgedriver_path${NC}"
        
        # Add to PATH suggestion
        echo -e "${YELLOW}To use this driver, add to your shell profile:${NC}"
        echo -e "export PATH=\"$(dirname "$edgedriver_path"):\$PATH\""
    fi
}

# Function to create cache directory
setup_cache() {
    echo -e "${BLUE}Setting up driver cache directory...${NC}"
    mkdir -p "$CACHE_DIR/chrome"
    mkdir -p "$CACHE_DIR/firefox"
    mkdir -p "$CACHE_DIR/edge"
    echo -e "${GREEN}✅ Cache directory created at: $CACHE_DIR${NC}"
}

# Function to update configuration
update_config() {
    echo -e "${BLUE}Updating configuration...${NC}"
    
    local config_file="./automation-framework/src/main/resources/config.properties"
    
    if [[ -f "$config_file" ]]; then
        # Backup original config
        cp "$config_file" "$config_file.backup"
        
        # Update driver paths
        local chromedriver_path=$(find "$DRIVERS_DIR" -name "chromedriver*" -type f | head -1)
        local geckodriver_path=$(find "$DRIVERS_DIR" -name "geckodriver*" -type f | head -1)
        local edgedriver_path=$(find "$DRIVERS_DIR" -name "msedgedriver*" -type f | head -1)
        
        if [[ -n "$chromedriver_path" ]]; then
            sed -i.bak "s|webdriver.chrome.path=|webdriver.chrome.path=$chromedriver_path|" "$config_file"
        fi
        
        if [[ -n "$geckodriver_path" ]]; then
            sed -i.bak "s|webdriver.firefox.path=|webdriver.firefox.path=$geckodriver_path|" "$config_file"
        fi
        
        if [[ -n "$edgedriver_path" ]]; then
            sed -i.bak "s|webdriver.edge.path=|webdriver.edge.path=$edgedriver_path|" "$config_file"
        fi
        
        echo -e "${GREEN}✅ Configuration updated${NC}"
    else
        echo -e "${YELLOW}⚠️  Configuration file not found: $config_file${NC}"
    fi
}

# Function to test drivers
test_drivers() {
    echo -e "${BLUE}Testing drivers...${NC}"
    
    # Test ChromeDriver
    local chromedriver_path=$(find "$DRIVERS_DIR" -name "chromedriver*" -type f | head -1)
    if [[ -n "$chromedriver_path" ]]; then
        echo -e "${YELLOW}Testing ChromeDriver...${NC}"
        if "$chromedriver_path" --version; then
            echo -e "${GREEN}✅ ChromeDriver working${NC}"
        else
            echo -e "${RED}❌ ChromeDriver test failed${NC}"
        fi
    fi
    
    # Test GeckoDriver
    local geckodriver_path=$(find "$DRIVERS_DIR" -name "geckodriver*" -type f | head -1)
    if [[ -n "$geckodriver_path" ]]; then
        echo -e "${YELLOW}Testing GeckoDriver...${NC}"
        if "$geckodriver_path" --version; then
            echo -e "${GREEN}✅ GeckoDriver working${NC}"
        else
            echo -e "${RED}❌ GeckoDriver test failed${NC}"
        fi
    fi
    
    # Test EdgeDriver
    local edgedriver_path=$(find "$DRIVERS_DIR" -name "msedgedriver*" -type f | head -1)
    if [[ -n "$edgedriver_path" ]]; then
        echo -e "${YELLOW}Testing EdgeDriver...${NC}"
        if "$edgedriver_path" --version; then
            echo -e "${GREEN}✅ EdgeDriver working${NC}"
        else
            echo -e "${RED}❌ EdgeDriver test failed${NC}"
        fi
    fi
}

# Main execution
main() {
    echo -e "${BLUE}Starting driver setup...${NC}"
    
    # Create directories
    setup_cache
    
    # Setup drivers based on user choice
    echo -e "${YELLOW}Which drivers would you like to install?${NC}"
    echo "1) ChromeDriver only"
    echo "2) GeckoDriver only"
    echo "3) EdgeDriver only"
    echo "4) All drivers"
    echo "5) Skip driver installation"
    
    read -p "Enter your choice (1-5): " choice
    
    case $choice in
        1)
            setup_chromedriver
            ;;
        2)
            setup_geckodriver
            ;;
        3)
            setup_edgedriver
            ;;
        4)
            setup_chromedriver
            setup_geckodriver
            setup_edgedriver
            ;;
        5)
            echo -e "${YELLOW}Skipping driver installation${NC}"
            ;;
        *)
            echo -e "${RED}Invalid choice. Exiting.${NC}"
            exit 1
            ;;
    esac
    
    # Update configuration
    update_config
    
    # Test drivers
    test_drivers
    
    echo ""
    echo -e "${GREEN}🎉 Driver setup completed!${NC}"
    echo ""
    echo -e "${BLUE}Next steps:${NC}"
    echo "1. Run the tests: mvn test"
    echo "2. Check the logs for driver initialization"
    echo "3. If issues persist, check the driver paths in config.properties"
    echo ""
    echo -e "${BLUE}Driver locations:${NC}"
    find "$DRIVERS_DIR" -type f -executable 2>/dev/null | while read -r file; do
        echo "  - $file"
    done
}

# Run main function
main "$@"
