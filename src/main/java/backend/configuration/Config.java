package backend.configuration;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config
{
    @Bean
    public RandomStringGenerator getRandomStringGenerator()
    {
        RandomStringGenerator generator  = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(Character::isLetterOrDigit).build();
        return generator;
    }
}