package vblank.languages;

import vblank.core.Config;
import vblank.core.Formatter;
import vblank.core.Tokenizer;

public abstract class AbstractFormatter {

    abstract Config dialectConfig();

    private Config cfg;

    /**
     * @param cfg Different set of configurations
     */
    public AbstractFormatter(Config cfg) {
        this.cfg = cfg;
    }

    /**
     * Formats DB2 query to make it easier to read
     *
     * @param query The DB2 query string
     * @return formatted string
     */
    public String format(String query) {
        Config dialectConfig = this.dialectConfig();
        Tokenizer tokenizer = new Tokenizer(this.cfg.toBuilder()
                .reservedWords(dialectConfig.reservedWords)
                .reservedToplevelWords(dialectConfig.reservedToplevelWords)
                .reservedNewlineWords(dialectConfig.reservedNewlineWords)
                .stringTypes(dialectConfig.stringTypes)
                .openParens(dialectConfig.openParens)
                .closeParens(dialectConfig.closeParens)
                .indexedPlaceholderTypes(dialectConfig.indexedPlaceholderTypes)
                .namedPlaceholderTypes(dialectConfig.namedPlaceholderTypes)
                .lineCommentTypes(dialectConfig.lineCommentTypes)
                .specialWordChars(dialectConfig.specialWordChars)
                .build());
        return new Formatter(this.cfg, tokenizer).format(query);
    }
}
