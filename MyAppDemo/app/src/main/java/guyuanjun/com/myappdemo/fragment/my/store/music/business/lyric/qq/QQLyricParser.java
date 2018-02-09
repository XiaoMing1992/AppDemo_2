package guyuanjun.com.myappdemo.fragment.my.store.music.business.lyric.qq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import guyuanjun.com.myappdemo.fragment.my.store.music.model.lyric.qq.SongInfo;
import guyuanjun.com.myappdemo.utils.MyUtils;


/**
 * QQ歌词解析器
 * 
 * @author Osmondy
 * 
 */
public class QQLyricParser
{
	
	/**
	 * 解析歌词信息
	 * 
	 * @param content
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException 
	 */
	public static List<SongInfo> parseSongInfo(String content) throws XmlPullParserException, IOException
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();
		
		InputStream is = new ByteArrayInputStream(content.getBytes());
		parser.setInput(is, "UTF-8");
		
		//ResultSet<SongInfo> resultSet = new ResultSet<SongInfo>();
		List<SongInfo> songInfos = new ArrayList<SongInfo>();
		SongInfo songInfo = null;
		
		int eventType = parser.getEventType();
		String tag = null;
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			tag = parser.getName();
			switch (eventType)
			{
				case XmlPullParser.START_TAG:
					if ("songcount".equalsIgnoreCase(tag))
					{
						//resultSet.setTotal(Integer.valueOf(parser.nextText()));
					}
					else if ("songinfo".equalsIgnoreCase(tag))
					{
						songInfo = new SongInfo();
						String id = parser.getAttributeValue(null, "id");
						int scroll = 0;
						String scrollString = parser.getAttributeValue(null, "scroll");
						if (MyUtils.isNumeric(scrollString))
						{
							scroll = Integer.parseInt(scrollString);
						}
						songInfo.setId(id);
						songInfo.setScroll(scroll);
					}
					else if ("seqnum".equalsIgnoreCase(tag))
					{
						songInfo.setSeqnum(Integer.parseInt(parser.nextText()));
					}
					else if ("name".equalsIgnoreCase(tag))
					{
						songInfo.setName(parser.nextText());
					}
					else if ("singername".equalsIgnoreCase(tag))
					{
						songInfo.setSingername(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if ("songinfo".equalsIgnoreCase(tag) && songInfo != null)
					{
						songInfos.add(songInfo);
						songInfo = null;
						//resultSet.setData(songInfos);
					}
					
					break;
				default:
					break;
			}
			
			eventType = parser.next();
		}
		
		return songInfos;
	}
	
}
