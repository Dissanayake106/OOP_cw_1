import java.util.*;

public class Find_Category {

    // Define 50 keywords for each category (all lowercase)
    private static final List<String> healthKeywords = Arrays.asList(
            "health", "wellness", "fitness", "nutrition", "exercise", "mental health", "workout", "diet", "weight loss", "gym",
            "yoga", "meditation", "healthy lifestyle", "stress", "body", "immunity", "diabetes", "chronic", "disease", "heart health",
            "healthy eating", "nutritionist", "sleep", "balance", "energy", "strength", "wellbeing", "physical activity", "medicine",
            "doctor", "treatment", "therapy", "mental", "medication", "mental illness", "healthcare", "well-being", "cancer", "recovery",
            "vaccination", "stress relief", "gym routine", "exercise program", "fitness goal", "physical health", "exercise routine",
            "mental clarity", "mental fitness", "self-care", "health tips", "weight management", "fat loss", "muscle gain", "immunity booster"
    );

    private static final List<String> sportsKeywords = Arrays.asList(
            "sports", "football", "cricket", "rugby", "athletics", "basketball", "tennis", "swimming", "volleyball", "game",
            "championship", "league", "competition", "tournament", "athlete", "fitness", "training", "score", "goal", "match",
            "exercise", "endurance", "team", "coach", "performance", "player", "medal", "win", "goalkeeper", "referee",
            "fitness", "workout", "play", "event", "sporting", "stadium", "track", "field", "marathon", "hockey", "fencing",
            "track and field", "boxing", "martial arts", "gymnastics", "racing", "champion", "scoreboard", "victory", "record", "competition rules"
    );

    private static final List<String> travelKeywords = Arrays.asList(
            "travel", "tourism", "vacation", "holiday", "trip", "destination", "destinations", "journey", "exploration", "adventure", "tourist",
            "hotel", "flight", "tour", "trip planning", "sightseeing", "itinerary", "backpacking", "destination", "cruise", "escape",
            "road trip", "beach", "mountain", "hiking","plains","sanctuary", "travel guide", "hotel booking", "culture", "explore", "wanderlust",
            "travel tips", "local experience", "travel blog", "holiday destination", "travel photography", "budget travel", "travel planning",
            "adventure tourism", "cycling","national park", "tourist spot", "safari", "expedition", "traveler", "globetrotter", "flight booking",
            "travel agency", "wet zone", "vlog", "travel experience", "adventure", "vacation spots", "tourist attractions", "national park"
    );

    private static final List<String> financialKeywords = Arrays.asList(
            "finance", "investment", "stocks", "market", "wealth", "money", "economy", "finance tips", "banking", "personal finance",
            "financial planning", "income", "expenses", "debt", "mortgage", "savings", "loan", "credit", "budget", "insurance",
            "retirement", "taxes", "financial advisor", "cryptocurrency", "real estate", "stocks market", "business", "stock trading",
            "trading", "financial analysis", "asset", "liabilities", "capital", "funding", "budgeting", "portfolio", "wealth management",
            "interest rate", "inflation", "currency exchange", "budget planning", "wealth growth", "stock exchange", "financial literacy",
            "financial goals", "financial strategy", "investment strategy", "savings account", "money management", "financial news", "startup",
            "income generation", "capital investment"
    );

    private static final List<String> techKeywords = Arrays.asList(
            "technology", "computer", "software", "hardware", "ai", "artificial intelligence", "machine learning", "programming",
            "data science", "python", "java", "website", "developer", "algorithm", "coding", "robotics", "automation", "innovation",
            "gadget", "internet", "network", "app", "mobile", "virtual", "augmented reality", "blockchain", "cloud computing",
            "startup", "enterprise", "tech", "program", "debug", "website", "web development", "tech news", "developer tools",
            "internet of things", "cloud storage", "data analysis", "cybersecurity", "database", "cloud", "tech industry"
    );

    // Method to determine category based on title and content
    public static String findCategory(String title, String content) {
        if (title == null || title.trim().isEmpty()) {
            return "Uncategorized";
        }

        // Preprocess title and content
        title = preprocessContent(title);
        content = preprocessContent(content);

        // Check title for category suggestion
        String categoryByTitle = getCategoryByKeywords(title);

        // If category is found from title, return that
        if (!categoryByTitle.equals("Uncategorized")) {
            return categoryByTitle;
        }

        // If no category from title, check the content
        return findCategoryByContent(content);
    }

    // Helper method to preprocess text: Convert to lowercase and remove punctuation
    private static String preprocessContent(String content) {
        return content.toLowerCase().replaceAll("[^a-z\\s]", ""); // Remove non-alphabetic characters and convert to lowercase
    }

    // Helper method to check the title for keyword-based categories
    private static String getCategoryByKeywords(String text) {
        // Split the title into words
        String[] words = text.split("\\s+");

        // Count occurrences of keywords for each category
        int healthCount = countKeywords(words, healthKeywords);
        int sportsCount = countKeywords(words, sportsKeywords);
        int travelCount = countKeywords(words, travelKeywords);
        int financialCount = countKeywords(words, financialKeywords);
        int techCount = countKeywords(words, techKeywords);

        // Determine the category with the highest match
        int[] counts = {healthCount, sportsCount, travelCount, financialCount, techCount};
        String[] categories = {"Health", "Sports", "Travel", "Financial", "Technology"};

        int maxCount = -1;
        String category = "Uncategorized";
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                category = categories[i];
            }
        }

        return category;
    }

    // Helper method to count keyword occurrences in text
    private static int countKeywords(String[] words, List<String> keywords) {
        int count = 0;
        for (String word : words) {
            if (keywords.contains(word)) {
                count++;
            }
        }
        return count;
    }

    // Method to determine category based on content (if no category from title)
    private static String findCategoryByContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "Uncategorized";
        }

        String[] words = content.split("\\s+");

        // Count keyword occurrences for each category
        int healthCount = countKeywords(words, healthKeywords);
        int sportsCount = countKeywords(words, sportsKeywords);
        int travelCount = countKeywords(words, travelKeywords);
        int financialCount = countKeywords(words, financialKeywords);
        int techCount = countKeywords(words, techKeywords);

        // Determine the category with the highest match
        int[] counts = {healthCount, sportsCount, travelCount, financialCount, techCount};
        String[] categories = {"Health", "Sports", "Travel", "Financial", "Technology"};

        int maxCount = -1;
        String category = "Uncategorized";
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                category = categories[i];
            }
        }

        return category;
    }
}
