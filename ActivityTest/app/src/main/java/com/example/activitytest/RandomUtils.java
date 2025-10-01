package com.example.activitytest;

import java.util.List;
import java.util.Random;

public class RandomUtils
{
    private final Random random = new Random(); // Create once

    public int getRandomNumberInRange10()
    {
        // random.nextInt(max - min + 1) + min
        return random.nextInt((10 - 1) + 1) + 1;
    }

    public Integer getRandomElementFromList(List<Integer> allowedValues)
    {
        if (allowedValues == null || allowedValues.isEmpty())
        {
            // Or throw an IllegalArgumentException, depending on desired behavior
            return null;
        }

        // Generate index from 0 to list.size()-1
        int randomIndex = random.nextInt(allowedValues.size());
        return allowedValues.get(randomIndex);
    }}
