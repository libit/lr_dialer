package com.lrcall.models;

public class UpdateInfo
{
	private String platform;
	private String version;
	private Integer versionCode;
	private String url;
	private String description;
	private Long addDateLong;

	public UpdateInfo()
	{
		super();
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Integer getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(Integer versionCode)
	{
		this.versionCode = versionCode;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Long getAddDateLong()
	{
		return addDateLong;
	}

	public void setAddDateLong(Long addDateLong)
	{
		this.addDateLong = addDateLong;
	}
}
