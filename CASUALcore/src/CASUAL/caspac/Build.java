/*
 * Copyright (C) 2015 adamoutler
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/ .
 */
package CASUAL.caspac;

import CASUAL.AudioHandler;
import CASUAL.FileOperations;
import CASUAL.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * build class is a reference to handle -build.properties information
 */
public final class Build {
    /**
     * Name of Script Developer.
     */
    private String developerName = "";
    /**
     * Name to display on donation button.
     */
    private String developerDonateButtonText = "";
    /**
     * Link to direct users to when donate button is clicked.
     */
    private String donateLink = "";
    /**
     * Title of UI window.
     */
    private String windowTitle = "";
    /**
     * True if use picture for banner. False if use text.
     */
    private boolean usePictureForBanner = false;
    /**
     * Image to use for banner pic. Generally CASPAC folder/CodeSource
     * -Logo.png
     */
    private String bannerPic = "";
    /**
     * Text to use for banner.
     */
    private String bannerText = "";
    /**
     * Text to be displayed on execute button.
     */
    private String executeButtonText = "Do It";
    /**
     * True if Audio is to be used by application for user enhanced
     * experience.
     */
    private boolean audioEnabled = AudioHandler.useSound;
    /**
     * True if controls should never be disabled.
     */
    private boolean alwaysEnableControls = false;
    /**
     * Properties file containing all properties of the -Build.properties
     * file
     */
    private Properties buildProp = new Properties();
    private Caspac caspac;

    /**
     * Creates a blank build.prop file
     * @param caspac reference to caspac
     */
    public Build(Caspac caspac){
        
    }
    
    
    /**
     * Accepts a -Build.properties file via InputStream.
     *
     * @param prop Properties file.
     * @param caspac to be associated with the build
     * @throws IOException when permissions problem exists.
     */
    public Build(InputStream prop, final Caspac caspac) throws IOException {
        this.caspac = caspac;
        Log.level4Debug("Loading build information from inputstream");
        buildProp.load(prop);
        loadPropsToVariables();
        Log.level4Debug(windowTitle + " - " + bannerText + " - " + developerName);
    }

    /**
     * loads and sets properties file
     *
     * @param prop build.properties file
     * @param caspac to be assocaiated with the build
     */
    public Build(Properties prop, final Caspac caspac) {
        this.caspac = caspac;
        Log.level4Debug("Loading build information from prop information");
        this.buildProp = prop;
        loadPropsToVariables();
    }

    /**
     * writes build properties to a file
     *
     * @param output file to write
     * @return true if file was written
     * @throws FileNotFoundException when file cannot be created
     * @throws IOException when permissions problem exists.
     */
    public boolean write(String output) throws FileNotFoundException, IOException {
        File f = new File(output);
        return write(f);
    }

    /**
     * writes build properties to a file
     *
     * @param output file to write
     * @return true if file was written
     * @throws FileNotFoundException when file cannot be created
     * @throws IOException when permissions problem exists.
     */
    public boolean write(File output) throws FileNotFoundException, IOException {
        FileOperations fo = new FileOperations();
        setPropsFromVariables();
        FileOutputStream fos = new FileOutputStream(output);
        buildProp.store(fos, "This properties file was generated by CASUAL");
        return fo.verifyExists(output.toString());
    }

    /**
     * gets the -Build.properties as an InputStream
     *
     * @return InputStream properties file for write-out.
     * @throws IOException when permissions problem exists.
     */
    public InputStream getBuildPropInputStream() throws IOException {
        setPropsFromVariables();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        buildProp.store(output, "This properties file was generated by CASUAL");
        return new ByteArrayInputStream(output.toByteArray());
    }

    /**
     * loads build properties from a map
     *
     * @param buildMap key,value pairs
     */
    private Build setPropsFromVariables() {
        buildProp.setProperty("Window.UsePictureForBanner", usePictureForBanner ? "True" : "False");
        buildProp.setProperty("Audio.Enabled", audioEnabled ? "True" : "False");
        buildProp.setProperty("Application.AlwaysEnableControls", alwaysEnableControls ? "True" : "False");
        buildProp.setProperty("Developer.DonateLink", donateLink);
        buildProp.setProperty("Developer.DonateToButtonText", developerDonateButtonText);
        buildProp.setProperty("Developer.Name", developerName);
        buildProp.setProperty("Window.ExecuteButtonText", executeButtonText);
        buildProp.setProperty("Window.BannerText", bannerText);
        buildProp.setProperty("Window.BannerPic", bannerPic);
        buildProp.setProperty("Window.Title", windowTitle);
        return this;
    }

    /**
     * sets properties to values stored in build.properties file.
     */
    void loadPropsToVariables() {
        if (buildProp.containsKey("Audio.Enabled")) {
            audioEnabled = buildProp.getProperty("Audio.Enabled", "").contains("rue");
            AudioHandler.useSound = audioEnabled;
        }
        usePictureForBanner = buildProp.getProperty("Window.UsePictureForBanner", "").contains("rue");
        developerDonateButtonText = buildProp.getProperty("Developer.DonateToButtonText", "");
        developerName = buildProp.getProperty("Developer.Name", "");
        donateLink = buildProp.getProperty("Developer.DonateLink", "");
        donateLink = buildProp.getProperty("Developer.DonateLink", "");
        executeButtonText = buildProp.getProperty("Window.ExecuteButtonText", "");
        bannerText = buildProp.getProperty("Window.BannerText", "");
        alwaysEnableControls = buildProp.getProperty("Application.AlwaysEnableControls", "").contains("rue");
        windowTitle = buildProp.getProperty("Window.Title", "");
    }

    /**
     * @return the developerName
     */
    public String getDeveloperName() {
        return developerName;
    }

    /**
     * @param developerName the developerName to set
     * @return this instance of Build
     */
    public Build setDeveloperName(String developerName) {
        this.developerName = developerName;
        return this;
    }

    /**
     * @return the developerDonateButtonText
     */
    public String getDeveloperDonateButtonText() {
        return developerDonateButtonText;
    }

    /**
     * @param developerDonateButtonText the developerDonateButtonText to set
     * @return this Build
     */
    public Build setDeveloperDonateButtonText(String developerDonateButtonText) {
        this.developerDonateButtonText = developerDonateButtonText;
        return this;
    }

    /**
     * @return the donateLink
     */
    public String getDonateLink() {
        return donateLink;
    }

    /**
     * @param donateLink the donateLink to set
     * @return  this Build
     */
    public Build setDonateLink(String donateLink) {
        this.donateLink = donateLink;
        return this;
    }

    /**
     * @return the windowTitle
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * @param windowTitle the windowTitle to set
     * @return  this Build
     */
    public Build setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
        return this;
    }

    /**
     * @return the usePictureForBanner
     */
    public boolean isUsePictureForBanner() {
        return usePictureForBanner;
    }

    /**
     * @param usePictureForBanner the usePictureForBanner to set
     * @return  this Build
     */
    public Build setUsePictureForBanner(boolean usePictureForBanner) {
        this.usePictureForBanner = usePictureForBanner;
        return this;
    }

    /**
     * @return the bannerPic
     */
    public String getBannerPic() {
        return bannerPic;
    }

    /**
     * @param bannerPic the bannerPic to set
     * @return  this Build
     */
    public Build setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
        return this;
    }

    /**
     * @return the bannerText
     */
    public String getBannerText() {
        return bannerText;
    }

    /**
     * @param bannerText the bannerText to set
     * @return  returns this Build object
     */
    public Build setBannerText(String bannerText) {
        this.bannerText = bannerText;
        return this;
    }

    /**
     * @return the executeButtonText
     */
    public String getExecuteButtonText() {
        return executeButtonText;
    }

    /**
     * @param executeButtonText the executeButtonText to set
     * @return this Build object
     */
    public Build setExecuteButtonText(String executeButtonText) {
        this.executeButtonText = executeButtonText;
        return this;
    }

    /**
     * @return the audioEnabled
     */
    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    /**
     * @param audioEnabled the audioEnabled to set
     * @return  this Build
     */
    public Build setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
        return this;
    }

    /**
     * @return the alwaysEnableControls
     */
    public boolean isAlwaysEnableControls() {
        return alwaysEnableControls;
    }

    /**
     * @param alwaysEnableControls the alwaysEnableControls to set
     * @return  this Build
     */
    public Build setAlwaysEnableControls(boolean alwaysEnableControls) {
        this.alwaysEnableControls = alwaysEnableControls;
        return this;
    }

    /**
     * @return the buildProp
     */
    public Properties getBuildProp() {
        return buildProp;
    }

    /**
     * @param buildProp the buildProp to set
     * @return  this Build
     */
    public Build setBuildProp(Properties buildProp) {
        this.buildProp = buildProp;
        return this;
    }

    /**
     * @return the build
     */
    public Caspac getBuild() {
        return caspac;
    }

    /**
     * @param build the build to set
     * @return  this Build
     */
    public Build setBuild(Caspac build) {
        this.caspac = build;
        return this;
    }
    
}
