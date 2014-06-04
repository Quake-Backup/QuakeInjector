/*
Copyright 2009 Hauke Rehfeld


This file is part of QuakeInjector.

QuakeInjector is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

QuakeInjector is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with QuakeInjector.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.haukerehfeld.quakeinjector;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class EngineStarter {
	private File quakeDir;
	private File quakeExe;
	private String quakeCmdline;

	private static boolean isMacOSX() {
		return System.getProperty("os.name").startsWith("Mac OS X");
	}
	
	private static boolean isMacApp(File app) {
		return app.isDirectory()
				&& app.getName().endsWith(".app");
	}
	
	private static boolean isExecutable(File exe) {
		return !exe.isDirectory() && exe.canExecute();
	}
	
	public static boolean isValidApp(File app) {
		if (!app.exists() || !app.canRead())
			return false;
		
		if (isMacOSX()  && isMacApp(app))
			return true;
		
		return isExecutable(app);
	}
	
	public static String errorMessageForApp(File app) {
		if (!app.exists()) {
			return "Doesn't exist!";
		}
		
		if (isMacOSX()) {
			if (!isMacApp(app) && !isExecutable(app)) {
				return "Must be an application or executable!";
			}
			return null;
			
		} else {
			if (app.isDirectory()) {
				return "Must be an executable file!";
			}
			else if (!app.canExecute()) {
				return "Cannot be executed!";
			}
			return null;
		}
	}
	
	private static File executableForApp(File app) {
		if (app != null && isMacApp(app)) {
			try {
				File contents = new File(app, "Contents");
				File plist = new File(contents, "Info.plist");
				File macOS = new File(contents, "MacOS");

				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = builder.parse(plist);

				XPath xpath = XPathFactory.newInstance().newXPath();
				XPathExpression expr = xpath.compile("/plist/dict/key/text()[.='CFBundleExecutable']/../following-sibling::string[1]/text()");
				String cfBundleExecutable = (String) expr.evaluate(document);

				File executable = new File(macOS, cfBundleExecutable);
				return executable;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return app;
	}
	
	public EngineStarter(File quakeDir, File quakeApp, Configuration.EngineCommandLine quakeCmdline) {
		this.quakeDir = quakeDir;
		setQuakeApp(quakeApp);
		this.quakeCmdline = quakeCmdline.get();
	}

	public Process start(String mapCmdline, String startmap) throws java.io.IOException {
		ArrayList<String> cmd = new ArrayList<String>(5);

		cmd.add(quakeExe.getAbsolutePath());
		//processbuilder doesn't like arguments with spaces
		if (quakeCmdline != null) {
			for (String s: quakeCmdline.split(" ")) { cmd.add(s); }
		}
		if (mapCmdline != null) {
			for (String s: mapCmdline.split(" ")) { cmd.add(s); }
		}
		cmd.add("+map");
		cmd.add(startmap);
		
		ProcessBuilder pb = new ProcessBuilder(cmd);
		pb.directory(quakeDir);
		pb.redirectErrorStream(true);

		System.out.println(cmd);
		
		Process p = pb.start();
		return p;
	}

	public void setQuakeDirectory(File dir) {
		this.quakeDir = dir;
	}

	public void setQuakeApp(File quakeApp) {
		this.quakeExe = executableForApp(quakeApp);
	}

	public void setQuakeCommandline(Configuration.EngineCommandLine cmdline) {
		this.quakeCmdline = cmdline.get();
	}

	public boolean checkPaths() {
		return (quakeExe.exists()
		        && !quakeExe.isDirectory()
		        && quakeExe.canRead()
		        && quakeExe.canExecute());
	}
}