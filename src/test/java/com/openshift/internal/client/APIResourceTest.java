/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.internal.client;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.IQuickstart;
import com.openshift.client.cartridge.ICartridge;
import com.openshift.client.cartridge.IEmbeddableCartridge;
import com.openshift.client.cartridge.IStandaloneCartridge;
import com.openshift.client.utils.CartridgeTestUtils;
import com.openshift.client.utils.QuickstartAssert;
import com.openshift.client.utils.QuickstartTestUtils;
import com.openshift.client.utils.Samples;
import com.openshift.client.utils.TestConnectionFactory;
import com.openshift.internal.client.cartridge.BaseCartridge;

/**
 * @author Xavier Coulon
 * @author Andre Dietisheim
 */
public class APIResourceTest extends TestTimer {

	private IOpenShiftConnection connection;
	private HttpClientMockDirector mockDirector;

	@Before
	public void setup() throws Throwable {
		this.mockDirector = new HttpClientMockDirector();
		mockDirector.mockGetQuickstarts(Samples.GET_API_QUICKSTARTS);
		connection = new TestConnectionFactory().getConnection(mockDirector.client());
	}

	@Test
	public void shouldListStandaloneCartridges() throws Throwable {
		// pre-conditions
		// operation
		final List<IStandaloneCartridge> cartridges = connection.getStandaloneCartridges();
		// verifications
		assertThat(cartridges)
				.hasSize(15)
				.onProperty("name")
				.contains(CartridgeTestUtils.NODEJS_06_NAME
						, CartridgeTestUtils.JBOSSAS_7_NAME
						, CartridgeTestUtils.JBOSSEAP_6_NAME
						, CartridgeTestUtils.JBOSSEWS_1_NAME
						, CartridgeTestUtils.JBOSSEWS_2_NAME)
				.excludes(CartridgeTestUtils.MONGODB_22_NAME
						, CartridgeTestUtils.MYSQL_51_NAME
						, CartridgeTestUtils.SWITCHYARD_06_NAME);
	}

	@Test
	public void shouldListEmbeddableCartridges() throws Throwable {
		// pre-conditions
		// operation
		final List<IEmbeddableCartridge> cartridges = connection.getEmbeddableCartridges();
		// verifications
		assertThat(cartridges)
				.hasSize(11)
				.onProperty("name")
				.excludes(CartridgeTestUtils.NODEJS_06_NAME
						, CartridgeTestUtils.JBOSSAS_7_NAME
						, CartridgeTestUtils.JBOSSEAP_6_NAME
						, CartridgeTestUtils.JBOSSEWS_1_NAME
						, CartridgeTestUtils.JBOSSEWS_2_NAME)
				.contains(CartridgeTestUtils.MONGODB_22_NAME
						, CartridgeTestUtils.MYSQL_51_NAME
						, CartridgeTestUtils.SWITCHYARD_06_NAME);
	}

	@Test
	public void shouldListCartridges() throws Throwable {
		// pre-conditions
		// operation
		final List<ICartridge> cartridges = connection.getCartridges();
		// verifications
		assertThat(cartridges)
				.hasSize(26)
				.onProperty("name")
				.contains(CartridgeTestUtils.NODEJS_06_NAME
						, CartridgeTestUtils.JBOSSAS_7_NAME
						, CartridgeTestUtils.JBOSSEAP_6_NAME
						, CartridgeTestUtils.JBOSSEWS_1_NAME
						, CartridgeTestUtils.JBOSSEWS_2_NAME
						, CartridgeTestUtils.MONGODB_22_NAME
						, CartridgeTestUtils.MYSQL_51_NAME
						, CartridgeTestUtils.SWITCHYARD_06_NAME);
	}

	@Test
	public void shouldListQuickstarts() throws Throwable {
		// pre-conditions

		// operation
		List<IQuickstart> quickstarts = connection.getQuickstarts();

		// verification
		assertThat(quickstarts)
				.hasSize(78)
				.onProperty("name")
				.contains("WordPress 3.x"
						, "Ruby on Rails"
						, "CapeDwarf"
						, "Django"
						, "CakePHP"
						, "Drupal 7"
						, "Reveal.js"
						, "Cartridge Development Kit"
						, "Go Language"
						, "AeroGear Push 0.X"
						, "WildFly 8"
						, "Ruby on Rails"
						, "Ruby on Rails");
	}

	@Test
	public void shouldUnmarshallWildfly8Quickstart() throws Throwable {
		// pre-conditions
		String wildfly = "WildFly 8";

		// operation
		IQuickstart wilfly8Quickstart = QuickstartTestUtils.getByName(wildfly, connection.getQuickstarts());

		// verification
		new QuickstartAssert(wilfly8Quickstart)
				.hasId("16766")
				.hasHref("https://www.openshift.com/quickstarts/wildfly-8")
				.hasName(wildfly)
				.hasSummary("WildFly is a flexible, lightweight, managed application runtime "
						+ "that helps you build amazing applications.\n\nThis cartridge provides WIldFly 8.0.0.Final")
				.hasWebsite("http://www.wildfly.org")
				.hasTags("java", "java_ee", "jboss")
				.hasLanguage("Java")
				.hasProvider("trusted")
				.hasCartridges(Arrays.<ICartridge> asList(CartridgeTestUtils.wildfly8()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldUnmarshallDjangoQuickstart() throws Throwable {
		// pre-conditions
		String django = "Django";

		// operation
		IQuickstart wilfly8Quickstart = QuickstartTestUtils.getByName(django, connection.getQuickstarts());

		// verification
		new QuickstartAssert(wilfly8Quickstart)
				.hasId("12730")
				.hasHref("https://www.openshift.com/quickstarts/django")
				.hasName(django)
				.hasSummary("A high-level Python web framework that encourages rapid development and clean, "
						+ "pragmatic design.\n\nDuring application creation the Django admin username and password"
						+ " will be written to a file called CREDENTIALS in your data directory.  "
						+ "You will need to SSH into your application to access these credentials.")
				.hasWebsite("https://www.djangoproject.com/")
				.hasTags("framework", "python")
				.hasLanguage("Python")
				.hasProvider("openshift")
				// expression := python-3|python-2, availble := python-3.3 and
				// pythong-2.6
				.hasCartridgeNames(Arrays.<String> asList("python-3.3", "python-2.6"));
	}

	@Test
	public void shouldUnmarshallDrupal8Quickstart() throws Throwable {
		// pre-conditions
		String drupal = "Drupal 8";

		// operation
		IQuickstart drupalQuickstart = QuickstartTestUtils.getByName(drupal, connection.getQuickstarts());

		// verification
		new QuickstartAssert(drupalQuickstart)
				.hasId("14942")
				.hasHref("https://www.openshift.com/quickstarts/drupal-8")
				.hasName(drupal)
				.hasSummary(
						"Try out the latest alpha releases of Drupal 8 on OpenShift. "
								+ "(Drupal is under active development, so any Drupal 8 sites should not be considered production-ready.)\n\n"
								+ "Drupal is an open source content management platform written in PHP powering millions of websites and applications. "
								+ "It is built, used, and supported by an active and diverse community of people around the world. "
								+ "Administrator user name and password are written to $OPENSHIFT_DATA_DIR/CREDENTIALS.")
				.hasWebsite("https://drupal.org")
				.hasTags("drupal")
				.hasLanguage("PHP")
				.hasProvider("community")
				.hasCartridges(
						Arrays.<ICartridge> asList(
								new BaseCartridge(
										new URL(
												"https://cartreflect-claytondev.rhcloud.com/reflect?github=phase2/openshift-php-fpm")),
								CartridgeTestUtils.mysql51(),
								new BaseCartridge(
										new URL(
												"https://cartreflect-claytondev.rhcloud.com/reflect?github=phase2/openshift-community-drush-master"))));
	}

	@Test
	public void shouldUnmarshallAerogearPushQuickstart() throws Throwable {
		// pre-conditions
		String aeroGearPush = "AeroGear Push 0.X";

		// operation
		IQuickstart aeroGarPushQuickstart = QuickstartTestUtils.getByName(aeroGearPush, connection.getQuickstarts());

		// verification
		new QuickstartAssert(aeroGarPushQuickstart)
				.hasId("15549")
				.hasHref("https://www.openshift.com/quickstarts/aerogear-push-0x")
				.hasName(aeroGearPush)
				.hasSummary(
						"The AeroGear UnifiedPush Server allows for sending native push messages to different mobile operation systems. "
								+ "This initial community version of the server supports Apple’s Push Notification Service (APNs), "
								+ "Google Cloud Messaging (GCM) and Mozilla’s SimplePush.\n\n"
								+ "It has a built in administrative console that makes it easy for developers of any type to create and manage "
								+ "push related aspects of their applications.")
				.hasWebsite("http://aerogear.org/")
				.hasTags("instant_app", "java", "messaging", "not_scalable", "xpaas")
				.hasLanguage("Java")
				.hasProvider("openshift")
				// expression :=
				// https://cartreflect-claytondev.rhcloud.com/reflect?github=aerogear/openshift-origin-cartridge-aerogear-push#AeroGear,
				// mysql-5
				.hasCartridges(
						Arrays.<ICartridge> asList(
								new BaseCartridge(
										new URL(
												"https://cartreflect-claytondev.rhcloud.com/reflect?github=aerogear/openshift-origin-cartridge-aerogear-push#AeroGear")),
								CartridgeTestUtils.mysql51()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldUnmarshallCactoQuickstart() throws Throwable {
		// pre-conditions

		// operation
		IQuickstart cactiQuickstart = QuickstartTestUtils.getByName("Cacti", connection.getQuickstarts());

		// verification
		new QuickstartAssert(cactiQuickstart)
				// expression := [{&quot;name&quot;:
				// &quot;php-5.3&quot;},{&quot;name&quot;:
				// &quot;mysql-5.1&quot;},{&quot;name&quot;:
				// &quot;cron-1.4&quot;}]
				.hasCartridgeNames(
						Collections.<String> singletonList("php-5.3"),
						Collections.<String> singletonList("mysql-5.1"),
						Collections.<String> singletonList("cron-1.4"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldOfferPhpMySqlCron() throws Throwable {
		// pre-conditions

		// operation
		IQuickstart cactiQuickstart = QuickstartTestUtils.getByName("Cacti", connection.getQuickstarts());

		// verification
		new QuickstartAssert(cactiQuickstart)
				// expression := [{&quot;name&quot;:
				// &quot;php-5.3&quot;},{&quot;name&quot;:
				// &quot;mysql-5.1&quot;},{&quot;name&quot;:
				// &quot;cron-1.4&quot;}]
				.hasCartridgeNames(
						Collections.<String> singletonList("php-5.3"),
						Collections.<String> singletonList("mysql-5.1"),
						Collections.<String> singletonList("cron-1.4"));
	}

	@Test
	public void shouldOfferDownloadableCartridge() throws Throwable {
		// pre-conditions

		// operation
		IQuickstart cactiQuickstart = QuickstartTestUtils.getByName("JBoss Fuse 6.1", connection.getQuickstarts());

		// verification
		new QuickstartAssert(cactiQuickstart)
				// expression := https://bit.ly/1fYSzhk
				.hasCartridges(
				Collections.<ICartridge> singletonList(new BaseCartridge(new URL("https://bit.ly/1fYSzhk"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldOfferPhp53Php54AndHaveInitialGitUrl() throws Throwable {
		// pre-conditions

		// operation
		IQuickstart laravelQuickstart = QuickstartTestUtils.getByName("Laravel 4.1 Quickstart",
				connection.getQuickstarts());

		// verification
		new QuickstartAssert(laravelQuickstart)
				.hasInitialGitUrl("https://github.com/muffycompo/openshift-laravel4-quickstart-app.git")
				// expression := "php-5.3|php-5.4, mysql-5.5",
				.hasCartridgeNames(
						Arrays.<String> asList("php-5.3", "php-5.4"),
						Arrays.asList("mysql-5.5"));
	}

}