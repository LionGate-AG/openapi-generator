/**
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openapitools.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;



/**
  * Model for testing model name same as property name
 **/

public class Name  {
  
  private Integer name;

  private Integer snakeCase;

  private String property;

  private Integer _123number;

 /**
   * Get name
   * @return name
  **/
  public Integer getName() {
    return name;
  }

  /**
    * Set name
  **/
  public void setName(Integer name) {
    this.name = name;
  }

  public Name name(Integer name) {
    this.name = name;
    return this;
  }

 /**
   * Get snakeCase
   * @return snakeCase
  **/
  public Integer getSnakeCase() {
    return snakeCase;
  }


 /**
   * Get property
   * @return property
  **/
  public String getProperty() {
    return property;
  }

  /**
    * Set property
  **/
  public void setProperty(String property) {
    this.property = property;
  }

  public Name property(String property) {
    this.property = property;
    return this;
  }

 /**
   * Get _123number
   * @return _123number
  **/
  public Integer get123number() {
    return _123number;
  }



  /**
    * Create a string representation of this pojo.
  **/
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Name {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    snakeCase: ").append(toIndentedString(snakeCase)).append("\n");
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("    _123number: ").append(toIndentedString(_123number)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

