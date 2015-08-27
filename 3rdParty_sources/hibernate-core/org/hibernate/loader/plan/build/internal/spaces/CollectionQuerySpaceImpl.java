/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
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
package org.hibernate.loader.plan.build.internal.spaces;

import org.hibernate.loader.plan.build.spi.ExpandingCollectionQuerySpace;
import org.hibernate.loader.plan.build.spi.ExpandingQuerySpaces;
import org.hibernate.loader.plan.spi.Join;
import org.hibernate.loader.plan.spi.JoinDefinedByMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.PropertyMapping;

/**
 * @author Steve Ebersole
 */
public class CollectionQuerySpaceImpl extends AbstractQuerySpace implements ExpandingCollectionQuerySpace {
	private final CollectionPersister persister;
	private JoinDefinedByMetadata elementJoin;
	private JoinDefinedByMetadata indexJoin;

	public CollectionQuerySpaceImpl(
			CollectionPersister persister,
			String uid,
			ExpandingQuerySpaces querySpaces,
			boolean canJoinsBeRequired) {
		super( uid, Disposition.COLLECTION, querySpaces, canJoinsBeRequired );
		this.persister = persister;
	}

	@Override
	public CollectionPersister getCollectionPersister() {
		return persister;
	}

	@Override
	public PropertyMapping getPropertyMapping() {
		return (PropertyMapping) persister;
	}

	public String[] toAliasedColumns(String alias, String propertyName) {
		final QueryableCollection queryableCollection = (QueryableCollection) persister;
		if ( propertyName.equals( CollectionPropertyNames.COLLECTION_ELEMENTS ) ) {
			return queryableCollection.getElementColumnNames( alias );
		}
		else if ( propertyName.equals( CollectionPropertyNames.COLLECTION_INDICES ) ) {
			return queryableCollection.getIndexColumnNames( alias );
		}
		else {
			throw new IllegalArgumentException(
					String.format(
							"Collection propertyName must be either %s or %s; instead it was %s.",
							CollectionPropertyNames.COLLECTION_ELEMENTS,
							CollectionPropertyNames.COLLECTION_INDICES,
							propertyName
					)
			);
		}
	}

	@Override
	public void addJoin(Join join) {
		if ( JoinDefinedByMetadata.class.isInstance( join ) ) {
			final JoinDefinedByMetadata joinDefinedByMetadata = (JoinDefinedByMetadata) join;
			if ( joinDefinedByMetadata.getJoinedPropertyName().equals( CollectionPropertyNames.COLLECTION_ELEMENTS ) ) {
				if ( elementJoin == null ) {
					elementJoin = joinDefinedByMetadata;
				}
				else {
					throw new IllegalStateException( "Attempt to add an element join, but an element join already exists." );
				}
			}
			else if ( joinDefinedByMetadata.getJoinedPropertyName().equals( CollectionPropertyNames.COLLECTION_INDICES ) ) {
				if ( indexJoin == null ) {
					indexJoin = joinDefinedByMetadata;
				}
				else {
					throw new IllegalStateException( "Attempt to add an index join, but an index join already exists." );
				}
			}
			else {
				throw new IllegalArgumentException(
						String.format(
								"Collection propertyName must be either %s or %s; instead the joined property name was %s.",
								CollectionPropertyNames.COLLECTION_ELEMENTS,
								CollectionPropertyNames.COLLECTION_INDICES,
								joinDefinedByMetadata.getJoinedPropertyName()
						)
				);
			}
		}
		internalGetJoins().add( join );
	}

	@Override
	public ExpandingQuerySpaces getExpandingQuerySpaces() {
		return super.getExpandingQuerySpaces();
	}

	public void addJoin(JoinDefinedByMetadata join) {
		addJoin( (Join) join );
	}
}
