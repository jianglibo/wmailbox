package cn.com.jinwang.pages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.io.IOUtils;
import org.wicketstuff.shiro.annotation.AnnotationsShiroAuthorizationStrategy;

import cn.com.jinwang.domain.LocalUser;
import cn.com.jinwang.factory.RepositoryFactory;
import cn.com.jinwang.initializer.EntityManagerFactoryHolder;
import cn.com.jinwang.utilbase.RandomUserGenerator;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 * 
 * @see cn.com.jinwang.pages.Start#main(String[])
 */
public class WicketApplicationPlain extends WebApplication {

  private Properties properties;

  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class<? extends WebPage> getHomePage() {
    return BaseHomePage.class;
  }



  /**
   * @see org.apache.wicket.Application#init()
   */
  @Override
  public void init() {
    super.init();
    getMarkupSettings().setStripWicketTags(true);

    properties = loadProperties();
    String unitName = properties.getProperty("jpa.unit");

    if (unitName == null || unitName.isEmpty()) {
      throw new WicketRuntimeException("jpa unit not configed in config.properties");
    }

    EntityManagerFactoryHolder.init(unitName);

    createSampleData();

    configureResourceBundles();

    // Configure Shiro
    AnnotationsShiroAuthorizationStrategy authz = new AnnotationsShiroAuthorizationStrategy();
    getSecuritySettings().setAuthorizationStrategy(authz);

    setHeaderResponseDecorator(new JavaScriptToBucketResponseDecorator("js-container-decorator"));

    // getSecuritySettings().setUnauthorizedComponentInstantiationListener(
    // new ShiroUnauthorizedComponentListener(LoginPage.class, UnauthorizedPage.class, authz));
    //
    // mountPage("account/login", LoginPage.class);
    // mountPage("account/logout", LogoutPage.class);
    // mountPage("admin", RequireAdminRolePage.class);
    // mountPage("view", RequireViewPermissionPage.class);
    // mountPage("auth", RequireAuthPage.class);
  }



  private void createSampleData() {
    String sampleCfg = properties.getProperty("db.sampledata");

    if (sampleCfg == null || sampleCfg.isEmpty()) {
      ;
    } else {
      long usernum = RepositoryFactory.getLocalUserRepository().countAll();
      if (usernum < 1) {
        for (LocalUser lu : RandomUserGenerator.randomUsers(50)) {
          RepositoryFactory.getLocalUserRepository().save(lu);
        }
      }
    }
  }


  private void configureResourceBundles() {
    ResourceBundles bundles = getResourceBundles();
    bundles.addCssBundle(WicketApplicationPlain.class, "pure-min.css");
  }

  /**
   * loads all configuration properties from disk
   * 
   * @return configuration properties
   */
  private Properties loadProperties() {
    Properties properties = new Properties();
    try {
      InputStream stream = getClass().getResourceAsStream("/config.properties");
      try {
        properties.load(stream);
      } finally {
        IOUtils.closeQuietly(stream);
      }
    } catch (IOException e) {
      throw new WicketRuntimeException(e);
    }
    return properties;
  }


  static class JavaScriptToBucketResponseDecorator implements IHeaderResponseDecorator {

    private String bucketName;

    public JavaScriptToBucketResponseDecorator(String bucketName) {
      this.bucketName = bucketName;
    }

    @Override
    public IHeaderResponse decorate(IHeaderResponse response) {
      return new JavaScriptFilteredIntoFooterHeaderResponse(response, bucketName);
    }

  }
}
