/*
 * Copyright (C) ST-Ericsson SA 2010. All rights reserved.
 *
 * This code is part of the NMF Toolset.
 *
 * The NMF Toolset is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * The NMF Toolset is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with the NMF Toolset. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package tools.idl.ast;

import java.io.File;

public class typeconst extends Type{
	public Type type;
	
	public typeconst(File file, tools.idl.parser.Token token, Type _type) {
		super(file, token);
		this.type = _type;
	}

	public String printType() {
		return "const " + type.printType();
	}
	
	public String declareParameter(String variable) {
        return printType() + " " + variable;
    }
}
