/*
 * PS3 Media Server, for streaming any medias to your PS3.
 * Copyright (C) 2008  A.Brochard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.pms.dlna;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.pms.formats.FormatType;
import net.pms.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RarredEntry extends DLNAResource implements IPushOutput {
	private static final Logger LOGGER = LoggerFactory.getLogger(RarredEntry.class);
	private String name;
	private File file;
	private String fileHeaderName;
	private long length;

	@Override
	protected String getThumbnailURL(DLNAImageProfile profile) {
		if (MediaType.isOf(getMediaType(), MediaType.IMAGE, MediaType.AUDIO)) { // no thumbnail support for now for rarred videos
			return null;
		}

		return super.getThumbnailURL(profile);
	}

	public RarredEntry(String name, File file, String fileHeaderName, long length) {
		this.fileHeaderName = fileHeaderName;
		this.name = name;
		this.file = file;
		this.length = length;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long length() {
		if (getPlayer() != null && getPlayer().type() != FormatType.IMAGE) {
			return DLNAMediaInfo.TRANS_SIZE;
		}

		return length;
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	// XXX unused
	@Deprecated
	public long lastModified() {
		return 0;
	}

	@Override
	public String getSystemName() {
		return FileUtil.getFileNameWithoutExtension(file.getAbsolutePath()) + "." + FileUtil.getExtension(name);
	}

	@Override
	public boolean isValid() {
		resolveFormat();
		setHasExternalSubtitles(FileUtil.isSubtitlesExists(file, null));
		return getFormat() != null;
	}

	@Override
	public boolean isUnderlyingSeekSupported() {
		return length() < MAX_ARCHIVE_SIZE_SEEK;
	}

	@Override
	public void push(final OutputStream out) throws IOException {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try (Archive rarFile = new Archive(file)) {
					FileHeader header = null;
					for (FileHeader fh : rarFile.getFileHeaders()) {
						if (fh.getFileNameString().equals(fileHeaderName)) {
							header = fh;
							break;
						}
					}
					if (header != null) {
						LOGGER.trace("Starting the extraction of " + header.getFileNameString());
						rarFile.extractFile(header, out);
					}
				} catch (RarException | IOException e) {
					LOGGER.debug("Unpack error, maybe it's normal, as backend can be terminated: {}", e.getMessage());
					LOGGER.trace("", e);
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						LOGGER.error(
							"An error occurred while trying to close the output when pushing \"{}\": {}",
							this,
							e.getMessage()
						);
						LOGGER.trace("", e);
					}
				}
			}
		};

		new Thread(r, "Rar Extractor").start();
	}

	@Override
	protected void resolveOnce() {
		if (!isVideo()) {
			return;
		}

		boolean found = false;

		if (!found) {
			if (getMedia() == null) {
				setMedia(new DLNAMediaInfo());
			}

			found = !getMedia().isMediaparsed() && !getMedia().isParsing();

			if (getFormat() != null) {
				InputFile input = new InputFile();
				input.setPush(this);
				input.setSize(length());
				getFormat().parse(getMedia(), input, null);
				if (getMedia() != null && getMedia().isSLS()) {
					setFormat(getMedia().getAudioVariantFormat());
				}
			}
		}
	}

	@Override
	public DLNAThumbnailInputStream getThumbnailInputStream() throws IOException {
		if (getMedia() != null && getMedia().getThumb() != null) {
			return getMedia().getThumbnailInputStream();
		}
		return super.getThumbnailInputStream();
	}
}
