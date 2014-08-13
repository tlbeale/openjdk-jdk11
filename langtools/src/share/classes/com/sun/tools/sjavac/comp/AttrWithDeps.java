/*
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.tools.sjavac.comp;

import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.code.Symbol;

/** Subclass to Attr that overrides reportDepedence.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class AttrWithDeps extends Attr {

    /** The dependency database
     */
    protected Dependencies deps;

    protected AttrWithDeps(Context context) {
        super(context);
        deps = Dependencies.instance(context);
    }

    public static void preRegister(Context context) {
        context.put(attrKey, new Context.Factory<Attr>() {
            public Attr make(Context c) {
                Attr instance = new AttrWithDeps(c);
                c.put(Attr.class, instance);
                return instance;
            }
        });
    }

    /** Collect dependencies in the enclosing class
     * @param from The enclosing class sym
     * @param to   The enclosing classes references this sym.
     * */
    @Override
    public void reportDependence(Symbol from, Symbol to) {
        // Capture dependencies between the packages.
        deps.collect(from.packge().fullname, to.packge().fullname);
    }
}
