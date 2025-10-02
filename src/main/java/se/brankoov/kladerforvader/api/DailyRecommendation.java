package se.brankoov.kladerforvader.api;


public record DailyRecommendation(String day,
                                  String temp,
                                  String weatherSummary,
                                  String recommendation) {}
