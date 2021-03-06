/*
 * Copyright (C) 2017 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.edu.hfut.dmic.webcollector.util;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

import com.heping.webcollector.model.CrawlDatum;
import org.junit.Test;

/**
 *
 * @author hu
 */
public class CrawlDatumTest {
    
    public static void main(String[] args) {
        Pattern.matches("abc", null);
    }
    
    public String testUrl="http://datahref.com";
    
    @Test
    public void testKey(){
        String key="test_key";
        CrawlDatum datum=new CrawlDatum(testUrl).key(key);
        assertEquals(key, datum.key());
    }
}
