package edu.berkeley.nlp.lm.io;

import java.util.List;

import edu.berkeley.nlp.lm.map.NgramMap;
import edu.berkeley.nlp.lm.util.Logger;

/**
 * Reader callback which adds n-grams to an NgramMap
 * 
 * @author adampauls
 * 
 * @param <V>
 *            Value type
 */
public final class NgramMapAddingCallback<V> implements ArpaLmReaderCallback<V>
{
	private final NgramMap<V> map;

	int warnCount = 0;

	public NgramMapAddingCallback(final NgramMap<V> map) {
		this.map = map;
	}

	@Override
	public void call(final int[] ngram, int startPos, int endPos, final V v, final String words) {
		final long add = map.put(ngram, startPos, endPos, v);
		if (add < 0) {
			if (warnCount >= 0 && warnCount < 10) {
				Logger.warn("Could not add line " + words + "\nThis is probabcly because the prefix or suff of the n-grams was not already in the map. This will be fixed in an upcoming release.");
				warnCount++;
			}
			if (warnCount > 10) warnCount = -1;
		}
	}

	@Override
	public void handleNgramOrderFinished(final int order) {
		map.handleNgramsFinished(order);
	}

	@Override
	public void cleanup() {
		map.trim();
	}

	@Override
	public void initWithLengths(final List<Long> numNGrams) {
		map.initWithLengths(numNGrams);
	}

}