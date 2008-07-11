package org.lamsfoundation.lams.tool.gmap.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.web.filter.LocaleFilter;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;

/**
 * Bundles the images for gmap in export portfolio
 * @author lfoxton
 *
 */
public class GmapImageBundler extends Bundler
{
	// list of images that need to be copied
	static String[] GMAP_IMAGES = {"blue_Marker.png", "paleblue_Marker.png", 
		"red_Marker.png", "yellow_Marker.png", "tree_closed.gif", "tree_open.gif"};
	
	public GmapImageBundler() {}
	
	public void bundle(HttpServletRequest request, Cookie[] cookies, String outputDirectory) throws GmapException
	{
		String gmapUrlPath = Configuration.get(ConfigurationKeys.SERVER_URL);
		if ( gmapUrlPath == null )  {
			log.error("Unable to get path to the LAMS Gmap URL from the configuration file - the exported portfolios will be missing parts of their Gmap images");
		} else {
			gmapUrlPath = gmapUrlPath + "/tool/" + GmapConstants.TOOL_SIGNATURE + "/images/";
			try
			{
				bundleViaHTTP(request, cookies, outputDirectory, gmapUrlPath);
			}
			catch (Exception e)
			{
				log.error("Failed to copy gmap images for export portfolio", e);
				throw new GmapException("Failed to copy gmap images for export portfolio", e);
			}
		
		}
	}
	
	
	/** 
	 * Bundles images by http
	 * 
	 * @param outputDirectory
	 * @param request
	 * @param cookies
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws MalformedURLException 
	 */
	private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies, String outputDirectory, String gmapUrlPath) 
		throws MalformedURLException, FileNotFoundException, IOException {
		
		String gmapImagesDirStr = outputDirectory+File.separator+"gmap_images";
		File gmapImagesDir = new File(gmapImagesDirStr);
		gmapImagesDir.mkdirs();
		
		for (String image : GMAP_IMAGES)
		{
			String url = gmapUrlPath + image;
			HttpUrlConnectionUtil.writeResponseToFile(url, gmapImagesDirStr, image , cookies); //cookies aren't really needed here.
		
			log.debug("Copying image from source: " + url + "to desitnation: " + gmapImagesDir.getAbsolutePath());
		}
	}
}
