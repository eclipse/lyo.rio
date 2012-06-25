/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution. 
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *    Steve Pitschke - initial API and implementation
 *******************************************************************************/

package org.eclipse.lyo.rio.query.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.lyo.rio.query.PName;
import org.eclipse.lyo.rio.query.SimpleTerm;
import org.eclipse.lyo.rio.query.SimpleTerm.Type;;

/**
 * Proxy implementation of {@link SimpleTerm} interface
 */
abstract class SimpleTermInvocationHandler implements InvocationHandler
{
    protected
    SimpleTermInvocationHandler(
        CommonTree tree,
        Type type
    )
    {
        this.tree = tree;
        this.type = type;
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object
    invoke(
        Object proxy,
        Method method,
        Object[] args
    ) throws Throwable
    {
        if (method.getName().equals("type")) {
            return type;
        }
        
        if (property != null) {
            return property;
        }
        
        if (tree == null) {
            return null;
        }
        
        String rawProperty = tree.getChild(0).toString();
        
        property = new PName();
        
        int colon = rawProperty.indexOf(':');
        
        if (colon < 0) {
            property.local = rawProperty;
        } else { 
            if (colon > 0) {
                property.prefix = rawProperty.substring(0, colon);
            }
            property.local = rawProperty.substring(colon + 1);
        }
        
        // XXX - Need to pass in oslc.prefixes from top to be able
        // to get namespace
        return property;
    }

    private PName property = null;
    protected final CommonTree tree;
    private final Type type;
}