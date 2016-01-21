package com.flaregames.slack.components;

public enum ConfigurationOption {
   WEBHOOK_URL("webhook.url"), CHANNELS("channels");

   private String suffix;

   private ConfigurationOption(String suffix) {
      this.suffix = suffix;
   }

   public String getBandanaKey() {
      return "slack." + suffix;
   }
}
