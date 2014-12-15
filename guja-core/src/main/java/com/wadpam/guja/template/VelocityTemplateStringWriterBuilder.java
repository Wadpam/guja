package com.wadpam.guja.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate localised string writers based on velocity templates and velocity contexts.
 *
 * @mattiaslevin
 */
public class VelocityTemplateStringWriterBuilder {

  {
    // Initialize Velocity engine in singleton mode
    final Properties p = new Properties();
    p.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
    p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    p.setProperty("resource.loader", "class");
    Velocity.init(p);
  }

  final private String templateName;
  final private VelocityContext vc;
  private Locale locale;


  public static VelocityTemplateStringWriterBuilder withTemplate(String name) {
    return new VelocityTemplateStringWriterBuilder(name, new VelocityContext(), Locale.getDefault());
  }


  private VelocityTemplateStringWriterBuilder(String templateName, VelocityContext vc, Locale locale) {
    this.templateName = templateName;
    this.vc = vc;
    this.locale = locale;
  }

  public VelocityTemplateStringWriterBuilder locale(Locale locale) {
    this.locale = locale;
    return this;
  }

  public VelocityTemplateStringWriterBuilder put(String key, Object value) {
    vc.put(key, value);
    return this;
  }

  public VelocityTemplateStringWriterBuilder put(Map<String, Object> map) {
    if (null != map) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }

  public StringWriter build() {

    // Load template based on locale
    // Will trow exception if template is not found
    Template template = Velocity.getTemplate(localizedTemplateName(templateName, locale));

    // Merge template and context
    StringWriter writer = new StringWriter();
    template.merge(vc, writer);

    return writer;
  }

  private static String localizedTemplateName(String templateName, Locale locale) {
    checkNotNull(templateName);

    if (null == locale) {
      return templateName;
    }

    String[] parts = templateName.split("\\.");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid velocity template name, must be in format name.vm");
    }

    return String.format("%s_%s.%s", parts[0], locale.getLanguage(), parts[1]);
	}

}
