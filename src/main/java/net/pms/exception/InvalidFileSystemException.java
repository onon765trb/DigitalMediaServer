/*
 * Digital Media Server, for streaming digital media to UPnP AV or DLNA
 * compatible devices based on PS3 Media Server and Universal Media Server.
 * Copyright (C) 2016 Digital Media Server developers.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/.
 */
package net.pms.exception;


/**
 * A checked {@link Exception} that indicates that the file system is unknown or
 * unsupported.
 *
 * @author Nadahar
 */
public class InvalidFileSystemException extends Exception {

	private static final long serialVersionUID = -4545843729375389876L;

	/**
	 * Creates a new exception with {@code null} as its detail message.
	 */
	public InvalidFileSystemException() {
		super();
	}

	/**
	 * Creates a new exception with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public InvalidFileSystemException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the specified code cause and a detail
	 * message of {@code (cause==null ? null : cause.toString())}.
	 *
	 * @param cause the {@link Throwable} which caused this
	 *            {@link InvalidFileSystemException}.
	 */
	public InvalidFileSystemException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception with the specified detail message and cause.
	 * <p>
	 * <b>Note</b>: The detail message associated with {@code cause} is
	 * <b><i>not</i></b> automatically incorporated in this exception's detail
	 * message.
	 *
	 * @param message the detail message.
	 * @param cause the {@link Throwable} which caused this
	 *            {@link InvalidFileSystemException}.
	 */
	public InvalidFileSystemException(String message, Throwable cause) {
		super(message, cause);
	}
}
