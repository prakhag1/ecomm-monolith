package demo.model;

public class Ad {

  private String redirectUrl;
  private String text;

  public Ad(AdBuilder builder) {
    this.redirectUrl = builder.redirectUrl;
    this.text = builder.text;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  // Builder Class
  public static class AdBuilder {

    private String redirectUrl;
    private String text;
    
	public AdBuilder setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
		return this;
	}

	public AdBuilder setText(String text) {
		this.text = text;
		return this;
	}

    public Ad build() {
      return new Ad(this);
    }
  }
}
