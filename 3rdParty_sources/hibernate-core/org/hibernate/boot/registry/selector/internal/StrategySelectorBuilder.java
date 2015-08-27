/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.boot.registry.selector.internal;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.selector.SimpleStrategyRegistrationImpl;
import org.hibernate.boot.registry.selector.StrategyRegistration;
import org.hibernate.boot.registry.selector.StrategyRegistrationProvider;
import org.hibernate.boot.registry.selector.spi.StrategySelectionException;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.dialect.CUBRIDDialect;
import org.hibernate.dialect.Cache71Dialect;
import org.hibernate.dialect.DB2390Dialect;
import org.hibernate.dialect.DB2400Dialect;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DerbyTenFiveDialect;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.dialect.DerbyTenSixDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.FrontBaseDialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.Ingres10Dialect;
import org.hibernate.dialect.Ingres9Dialect;
import org.hibernate.dialect.IngresDialect;
import org.hibernate.dialect.InterbaseDialect;
import org.hibernate.dialect.JDataStoreDialect;
import org.hibernate.dialect.MckoiDialect;
import org.hibernate.dialect.MimerSQLDialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.PointbaseDialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.dialect.ProgressDialect;
import org.hibernate.dialect.SAPDBDialect;
import org.hibernate.dialect.SQLServer2005Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.Sybase11Dialect;
import org.hibernate.dialect.SybaseASE157Dialect;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.dialect.SybaseAnywhereDialect;
import org.hibernate.dialect.TeradataDialect;
import org.hibernate.dialect.TimesTenDialect;
import org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory;
import org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory;
import org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory;
import org.hibernate.engine.transaction.jta.platform.internal.BitronixJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.BorlandEnterpriseServerJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.JBossAppServerJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.JBossStandAloneJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.JOTMJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.JOnASJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.JRun4JtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.OC4JJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.OrionJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.ResinJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.SunOneJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.WebSphereExtendedJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.WebSphereJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.WeblogicJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.engine.transaction.spi.TransactionFactory;
import org.hibernate.event.internal.EntityCopyAllowedLoggedObserver;
import org.hibernate.event.internal.EntityCopyAllowedObserver;
import org.hibernate.event.internal.EntityCopyNotAllowedObserver;
import org.hibernate.event.spi.EntityCopyObserver;
import org.hibernate.hql.spi.MultiTableBulkIdStrategy;
import org.hibernate.hql.spi.PersistentTableBulkIdStrategy;
import org.hibernate.hql.spi.TemporaryTableBulkIdStrategy;

import org.jboss.logging.Logger;

/**
 * Builder for StrategySelector instances.
 *
 * @author Steve Ebersole
 */
public class StrategySelectorBuilder {
	private static final Logger log = Logger.getLogger( StrategySelectorBuilder.class );

	private final List<StrategyRegistration> explicitStrategyRegistrations = new ArrayList<StrategyRegistration>();

	/**
	 * Adds an explicit (as opposed to discovered) strategy registration.
	 *
	 * @param strategy The strategy
	 * @param implementation The strategy implementation
	 * @param name The registered name
	 * @param <T> The type of the strategy.  Used to make sure that the strategy and implementation are type
	 * compatible.
	 */
	@SuppressWarnings("unchecked")
	public <T> void addExplicitStrategyRegistration(Class<T> strategy, Class<? extends T> implementation, String name) {
		addExplicitStrategyRegistration( new SimpleStrategyRegistrationImpl<T>( strategy, implementation, name ) );
	}

	/**
	 * Adds an explicit (as opposed to discovered) strategy registration.
	 *
	 * @param strategyRegistration The strategy implementation registration.
	 * @param <T> The type of the strategy.  Used to make sure that the strategy and implementation are type
	 * compatible.
	 */
	public <T> void addExplicitStrategyRegistration(StrategyRegistration<T> strategyRegistration) {
		if ( !strategyRegistration.getStrategyRole().isInterface() ) {
			// not good form...
			log.debug( "Registering non-interface strategy : " + strategyRegistration.getStrategyRole().getName()  );
		}

		if ( ! strategyRegistration.getStrategyRole().isAssignableFrom( strategyRegistration.getStrategyImplementation() ) ) {
			throw new StrategySelectionException(
					"Implementation class [" + strategyRegistration.getStrategyImplementation().getName()
							+ "] does not implement strategy interface ["
							+ strategyRegistration.getStrategyRole().getName() + "]"
			);
		}
		explicitStrategyRegistrations.add( strategyRegistration );
	}

	/**
	 * Builds the selector.
	 *
	 * @param classLoaderService The class loading service used to (attempt to) resolve any un-registered
	 * strategy implementations.
	 *
	 * @return The selector.
	 */
	public StrategySelector buildSelector(ClassLoaderService classLoaderService) {
		final StrategySelectorImpl strategySelector = new StrategySelectorImpl( classLoaderService );

		// build the baseline...
		addDialects( strategySelector );
		addJtaPlatforms( strategySelector );
		addTransactionFactories( strategySelector );
		addMultiTableBulkIdStrategies( strategySelector );
		addEntityCopyObserverStrategies( strategySelector );

		// apply auto-discovered registrations
		for ( StrategyRegistrationProvider provider : classLoaderService.loadJavaServices( StrategyRegistrationProvider.class ) ) {
			for ( StrategyRegistration discoveredStrategyRegistration : provider.getStrategyRegistrations() ) {
				applyFromStrategyRegistration( strategySelector, discoveredStrategyRegistration );
			}
		}

		// apply customizations
		for ( StrategyRegistration explicitStrategyRegistration : explicitStrategyRegistrations ) {
			applyFromStrategyRegistration( strategySelector, explicitStrategyRegistration );
		}

		return strategySelector;
	}

	@SuppressWarnings("unchecked")
	private <T> void applyFromStrategyRegistration(StrategySelectorImpl strategySelector, StrategyRegistration<T> strategyRegistration) {
		for ( String name : strategyRegistration.getSelectorNames() ) {
			strategySelector.registerStrategyImplementor(
					strategyRegistration.getStrategyRole(),
					name,
					strategyRegistration.getStrategyImplementation()
			);
		}
	}

	private void addDialects(StrategySelectorImpl strategySelector) {
		addDialect( strategySelector, Cache71Dialect.class );
		addDialect( strategySelector, CUBRIDDialect.class );
		addDialect( strategySelector, DB2Dialect.class );
		addDialect( strategySelector, DB2390Dialect.class );
		addDialect( strategySelector, DB2400Dialect.class );
		addDialect( strategySelector, DerbyTenFiveDialect.class );
		addDialect( strategySelector, DerbyTenSixDialect.class );
		addDialect( strategySelector, DerbyTenSevenDialect.class );
		addDialect( strategySelector, FirebirdDialect.class );
		addDialect( strategySelector, FrontBaseDialect.class );
		addDialect( strategySelector, H2Dialect.class );
		addDialect( strategySelector, HSQLDialect.class );
		addDialect( strategySelector, InformixDialect.class );
		addDialect( strategySelector, IngresDialect.class );
		addDialect( strategySelector, Ingres9Dialect.class );
		addDialect( strategySelector, Ingres10Dialect.class );
		addDialect( strategySelector, InterbaseDialect.class );
		addDialect( strategySelector, JDataStoreDialect.class );
		addDialect( strategySelector, MckoiDialect.class );
		addDialect( strategySelector, MimerSQLDialect.class );
		addDialect( strategySelector, MySQL5Dialect.class );
		addDialect( strategySelector, MySQL5InnoDBDialect.class );
		addDialect( strategySelector, MySQL5Dialect.class );
		addDialect( strategySelector, MySQL5InnoDBDialect.class );
		addDialect( strategySelector, Oracle8iDialect.class );
		addDialect( strategySelector, Oracle9iDialect.class );
		addDialect( strategySelector, Oracle10gDialect.class );
		addDialect( strategySelector, PointbaseDialect.class );
		addDialect( strategySelector, PostgresPlusDialect.class );
		addDialect( strategySelector, PostgreSQL81Dialect.class );
		addDialect( strategySelector, PostgreSQL82Dialect.class );
		addDialect( strategySelector, PostgreSQL9Dialect.class );
		addDialect( strategySelector, ProgressDialect.class );
		addDialect( strategySelector, SAPDBDialect.class );
		addDialect( strategySelector, SQLServerDialect.class );
		addDialect( strategySelector, SQLServer2005Dialect.class );
		addDialect( strategySelector, SQLServer2008Dialect.class );
		addDialect( strategySelector, Sybase11Dialect.class );
		addDialect( strategySelector, SybaseAnywhereDialect.class );
		addDialect( strategySelector, SybaseASE15Dialect.class );
		addDialect( strategySelector, SybaseASE157Dialect.class );
		addDialect( strategySelector, TeradataDialect.class );
		addDialect( strategySelector, TimesTenDialect.class );
	}

	private void addDialect(StrategySelectorImpl strategySelector, Class<? extends Dialect> dialectClass) {
		String simpleName = dialectClass.getSimpleName();
		if ( simpleName.endsWith( "Dialect" ) ) {
			simpleName = simpleName.substring( 0, simpleName.length() - "Dialect".length() );
		}
		strategySelector.registerStrategyImplementor( Dialect.class, simpleName, dialectClass );
	}

	private void addJtaPlatforms(StrategySelectorImpl strategySelector) {
		addJtaPlatforms(
				strategySelector,
				BorlandEnterpriseServerJtaPlatform.class,
				"Borland",
				"org.hibernate.service.jta.platform.internal.BorlandEnterpriseServerJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				BitronixJtaPlatform.class,
				"Bitronix",
				"org.hibernate.service.jta.platform.internal.BitronixJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				JBossAppServerJtaPlatform.class,
				"JBossAS",
				"org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				JBossStandAloneJtaPlatform.class,
				"JBossTS",
				"org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				JOnASJtaPlatform.class,
				"JOnAS",
				"org.hibernate.service.jta.platform.internal.JOnASJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				JOTMJtaPlatform.class,
				"JOTM",
				"org.hibernate.service.jta.platform.internal.JOTMJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				JRun4JtaPlatform.class,
				"JRun4",
				"org.hibernate.service.jta.platform.internal.JRun4JtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				OC4JJtaPlatform.class,
				"OC4J",
				"org.hibernate.service.jta.platform.internal.OC4JJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				OrionJtaPlatform.class,
				"Orion",
				"org.hibernate.service.jta.platform.internal.OrionJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				ResinJtaPlatform.class,
				"Resin",
				"org.hibernate.service.jta.platform.internal.ResinJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				SunOneJtaPlatform.class,
				"SunOne",
				"org.hibernate.service.jta.platform.internal.SunOneJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				WeblogicJtaPlatform.class,
				"Weblogic",
				"org.hibernate.service.jta.platform.internal.WeblogicJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				WebSphereJtaPlatform.class,
				"WebSphere",
				"org.hibernate.service.jta.platform.internal.WebSphereJtaPlatform"
		);

		addJtaPlatforms(
				strategySelector,
				WebSphereExtendedJtaPlatform.class,
				"WebSphereExtended",
				"org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform"
		);
	}

	private void addJtaPlatforms(StrategySelectorImpl strategySelector, Class<? extends JtaPlatform> impl, String... names) {
		for ( String name : names ) {
			strategySelector.registerStrategyImplementor( JtaPlatform.class, name, impl );
		}
	}

	private void addTransactionFactories(StrategySelectorImpl strategySelector) {
		strategySelector.registerStrategyImplementor( TransactionFactory.class, JdbcTransactionFactory.SHORT_NAME, JdbcTransactionFactory.class );
		strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.JDBCTransactionFactory", JdbcTransactionFactory.class );

		strategySelector.registerStrategyImplementor( TransactionFactory.class, JtaTransactionFactory.SHORT_NAME, JtaTransactionFactory.class );
		strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.JTATransactionFactory", JtaTransactionFactory.class );

		strategySelector.registerStrategyImplementor( TransactionFactory.class, CMTTransactionFactory.SHORT_NAME, CMTTransactionFactory.class );
		strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.CMTTransactionFactory", CMTTransactionFactory.class );
	}

	private void addMultiTableBulkIdStrategies(StrategySelectorImpl strategySelector) {
		strategySelector.registerStrategyImplementor(
				MultiTableBulkIdStrategy.class,
				PersistentTableBulkIdStrategy.SHORT_NAME,
				PersistentTableBulkIdStrategy.class
		);
		strategySelector.registerStrategyImplementor(
				MultiTableBulkIdStrategy.class,
				TemporaryTableBulkIdStrategy.SHORT_NAME,
				TemporaryTableBulkIdStrategy.class
		);
	}

	private void addEntityCopyObserverStrategies(StrategySelectorImpl strategySelector) {
		strategySelector.registerStrategyImplementor(
				EntityCopyObserver.class,
				EntityCopyNotAllowedObserver.SHORT_NAME,
				EntityCopyNotAllowedObserver.class
		);
		strategySelector.registerStrategyImplementor(
				EntityCopyObserver.class,
				EntityCopyAllowedObserver.SHORT_NAME,
				EntityCopyAllowedObserver.class
		);
		strategySelector.registerStrategyImplementor(
				EntityCopyObserver.class,
				EntityCopyAllowedLoggedObserver.SHORT_NAME,
				EntityCopyAllowedLoggedObserver.class
		);
	}
}
