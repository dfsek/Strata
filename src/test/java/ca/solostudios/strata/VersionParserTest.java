/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionParserTest.java is part of Strata
 * Last modified on 24-09-2021 08:02 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * STRATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.strata;


import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static ca.solostudios.strata.Versions.parseVersion;


class VersionParserTest {
    @Test
    void testCoreParsing() {
        String[] validVersions = {
                "0.0.4",
                "1.0.0",
                "1.1.7",
                "1.2.3",
                "10.20.30",
                "2.0.0",
                "99999999999999999999999.999999999999999999.99999999999999999"
        };
    
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = parseVersion(version);
                assertEquals(version, ver.getFormatted());
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testPreReleaseParsing() {
        String[] validVersions = {
                "1.0.0-0A.is.legal",
                "1.0.0-alpha",
                "1.0.0-alpha.0valid",
                "1.0.0-alpha.1",
                "1.0.0-alpha.beta",
                "1.0.0-alpha.beta.1",
                "1.0.0-alpha0.valid",
                "1.0.0-beta",
                "1.2.3----RC-SNAPSHOT.12.9.1--.12",
                "1.2.3-SNAPSHOT-123",
                "1.2.3-beta",
                "10.2.3-DEV-SNAPSHOT",
                "2.0.1-alpha.1227"
        };
        
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = parseVersion(version);
                assertEquals(version, ver.getFormatted());
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testBuildMetadataParsing() {
        String[] validVersions = {
                "1.0.0+0.build.1-rc.10000aaa-kk-0.1",
                "1.1.2+meta",
                "1.1.2+meta-valid",
                "2.0.0+build.1848"
        };
        
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = parseVersion(version);
                assertEquals(version, ver.getFormatted());
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testPreReleaseAndBuildMetadataParsing() {
        String[] validVersions = {
                "1.0.0-alpha+beta",
                "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay",
                "1.0.0-rc.1+build.1",
                "1.1.2-prerelease+meta",
                "1.2.3----R-S.12.9.1--.12+meta",
                "1.2.3----RC-SNAPSHOT.12.9.1--.12+788",
                "2.0.0-rc.1+build.123"
        };
    
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = parseVersion(version);
                assertEquals(version, ver.getFormatted());
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testInvalidVersions() throws ParseException {
        String[] invalidVersions = {
                "+invalid",
                "+justmeta",
                "-1.0.3-gamma+b7718",
                "-alpha.",
                "-invalid",
                "-invalid+invalid",
                "-invalid.01",
                "01.1.1",
                "1",
                "1.0.0-alpha.",
                "1.0.0-alpha..",
                "1.0.0-alpha.......1",
                "1.0.0-alpha......1",
                "1.0.0-alpha.....1",
                "1.0.0-alpha....1",
                "1.0.0-alpha...1",
                "1.0.0-alpha..1",
                "1.0.0-alpha_beta",
                "1.01.1",
                "1.1.01",
                "1.1.2+.123",
                "1.2",
                "1.2",
                "1.2-RC-SNAPSHOT",
                "1.2-SNAPSHOT",
                "1.2. 3",
                "1.2.3+@",
                "1.2.3+b1+b2",
                "1.2.3-+",
                "1.2.3-0123",
                "1.2.3-0123.0123",
                "1.2.3-@",
                "1.2.3-a+b..",
                "1.2.3-b.+b",
                "1.2.3-be$ta",
                "1.2.3-rc!",
                "1.2.3-rc.",
                "1.2.3-rc..",
                "1.2.3.DEV",
                "1.2.31.2.3----RC-SNAPSHOT.12.09.1--..12+788",
                "1.2.3=alpha",
                "1.2.3~beta",
                "1.2.?",
                "1._.3",
                "9.8.7+meta+meta",
                "9.8.7-whatever+meta+meta",
                "99999999999999999999999.999999999999999999.99999999999999999----RC-SNAPSHOT.12.09.1--------------------------------..12",
                "[.2.3",
                "alpha",
                "alpha+beta",
                "alpha.",
                "alpha..",
                "alpha.1",
                "alpha.beta",
                "alpha.beta.1",
                "alpha_beta",
                "beta",
                "v1.2.3"
        };
        
        for (String version : invalidVersions)
            assertThrows(ParseException.class, () -> {
                Version ver = parseVersion(version);
                assertEquals(version, ver.getFormatted()); // should fail
            }, String.format("Succeeded parsing of invalid version '%s'.", version));
    }
}