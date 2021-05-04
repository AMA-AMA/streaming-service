package com.epam.audio_streaming.service;

import com.epam.audio_streaming.model.Source;

import java.io.InputStream;

public interface SongInfoService {

    Long saveAudio(InputStream input, Source source) throws Exception;
}
