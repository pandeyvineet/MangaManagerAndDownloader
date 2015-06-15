package de.andreasgiemza.mangadownloader.sites.implementations.espaņol;

import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.extend.MangaHere;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEspaņol extends MangaHere implements Site {

	public MangaHereEspaņol() {
		super("Manga Here (Espaņol)", "http://es.mangahere.co", Arrays
				.asList("Espaņol"), false);
	}
}
