/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Version.java is part of Strata
 * Last modified on 17-07-2021 05:45 p.m.
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

package com.solostudios.strata.version;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class Version implements Comparable<Version>, Formattable {
    private final int major;
    
    private final int minor;
    
    private final int patch;
    
    @NotNull
    private final PreRelease preRelease;
    
    @NotNull
    private final BuildMetadata buildMetadata;
    
    @Contract(pure = true)
    public Version(int major, int minor, int patch, @NotNull PreRelease preRelease, @NotNull BuildMetadata buildMetadata) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
        this.buildMetadata = buildMetadata;
    }
    
    @Contract(pure = true)
    public Version(int major, int minor, int patch, @NotNull PreRelease preRelease) {
        this(major, minor, patch, preRelease, BuildMetadata.NULL_BUILD_METADATA);
    }
    
    @Contract(pure = true)
    public Version(int major, int minor, int patch, @NotNull BuildMetadata buildMetadata) {
        this(major, minor, patch, PreRelease.NULL_PRE_RELEASE, buildMetadata);
    }
    
    @Contract(pure = true)
    public Version(int major, int minor, int patch) {
        this(major, minor, patch, PreRelease.NULL_PRE_RELEASE, BuildMetadata.NULL_BUILD_METADATA);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(String version) {
        return new Builder(version);
    }
    
    @Override
    public int compareTo(@NotNull Version o) {
        if (major > o.major)
            return 1;
        else if (major < o.major)
            return -1;
        
        if (minor > o.minor)
            return 1;
        else if (minor < o.minor)
            return -1;
        
        return preRelease.compareTo(o.preRelease);
    }
    
    @Override
    public String toString() {
        return String.format("Version{major=%d, minor=%d, patch=%d, preRelease=%s, buildMetadata=%s}",
                             major, minor, patch, preRelease, buildMetadata);
    }
    
    public int getMajor() {
        return major;
    }
    
    public int getMinor() {
        return minor;
    }
    
    public int getPatch() {
        return patch;
    }
    
    @NotNull
    public PreRelease getPreRelease() {
        return preRelease;
    }
    
    @NotNull
    public BuildMetadata getBuildMetadata() {
        return buildMetadata;
    }
    
    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s%s%s", major, major, patch, preRelease, buildMetadata);
    }
    
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Version version = (Version) o;
        
        if (major != version.major) return false;
        if (minor != version.minor) return false;
        if (patch != version.patch) return false;
        if (!preRelease.equals(version.preRelease)) return false;
        return buildMetadata.equals(version.buildMetadata);
    }
    
    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + preRelease.hashCode();
        result = 31 * result + buildMetadata.hashCode();
        return result;
    }
    
    
    public static final class Builder {
        @NotNull
        private String version = "";
        
        @NotNull
        private String preRelease = "";
        
        @NotNull
        private String buildMetadata = "";
        
        @Contract(pure = true)
        private Builder() {
        }
        
        @Contract(pure = true)
        private Builder(@NotNull String version) {
            this.version = version;
        }
        
        @NotNull
        @Contract(pure = true)
        public String getVersion() {
            return version;
        }
        
        @Contract(value = "_ -> this", mutates = "this")
        public Builder setVersion(@NotNull String version) {
            this.version = version;
            return this;
        }
        
        @NotNull
        @Contract(pure = true)
        public String getPreRelease() {
            return preRelease;
        }
        
        @Contract(value = "_ -> this", mutates = "this")
        public Builder setPreRelease(@NotNull String preRelease) {
            this.preRelease = preRelease;
            return this;
        }
        
        @NotNull
        @Contract(pure = true)
        public String getBuildMetadata() {
            return buildMetadata;
        }
        
        @Contract(value = "_ -> this", mutates = "this")
        public Builder setBuildMetadata(@NotNull String buildMetadata) {
            this.buildMetadata = buildMetadata;
            return this;
        }
    }
}