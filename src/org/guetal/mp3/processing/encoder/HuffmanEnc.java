/*
 * HuffmanEnc.java
 *
 * Created on 7 gennaio 2007, 16.54
 */

package org.guetal.mp3.processing.encoder;

import org.guetal.mp3.processing.commons.CommonMethods;
import org.guetal.mp3.processing.commons.data.Channel;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;
import org.guetal.mp3.processing.commons.data.SideInfo;


/**
 *  Class providing methods for Huffman encoding
 *
 *  @author Thomas Gualino
 */
public final class HuffmanEnc {
    
    //private InternalFlags gfc;
    private int region0_count = 0;
    private int region1_count = 1;
    
    private final int FREQUENCY_LINES = 576;
    
    private int bigvalues_region  = FREQUENCY_LINES;
    private int count1_region     = FREQUENCY_LINES;
    private int zero_region       = FREQUENCY_LINES;
    
    private int x, y, v, w;
    private int code = 0, ext = 0, cbits  = 0, xbits = 0;
    
    
    int cont = 0;
    
    /* Huffman tables */
    
    private static final short      t0HB[]   = {0};
    private static final short      t1HB[]   = {1, 1, 1, 0};
    private static final short      t2HB[]   = {1, 2, 1, 3, 1, 1, 3, 2, 0};
    private static final short      t3HB[]   = {3, 2, 1, 1, 1, 1, 3, 2, 0};
    private static final short      t5HB[]   = {1, 2, 6, 5, 3, 1, 4, 4, 7, 5, 7, 1, 6, 1, 1, 0};
    private static final short      t6HB[]   = {7, 3, 5, 1, 6, 2, 3, 2, 5, 4, 4, 1, 3, 3, 2, 0};
    private static final short      t7HB[]   = {1, 2, 10, 19, 16, 10, 3, 3, 7, 10, 5, 3, 11, 4, 13, 17, 8, 4, 12, 11, 18, 15, 11, 2, 7, 6, 9, 14, 3, 1, 6, 4, 5, 3, 2, 0};
    private static final short      t8HB[]   = {3, 4, 6, 18, 12, 5, 5, 1, 2, 16, 9, 3, 7, 3, 5, 14, 7, 3, 19, 17, 15, 13, 10, 4, 13, 5, 8, 11, 5, 1, 12, 4, 4, 1, 1, 0};
    private static final short      t9HB[]   = {7, 5, 9, 14, 15, 7, 6, 4, 5, 5, 6, 7, 7, 6, 8, 8, 8, 5, 15, 6, 9, 10, 5, 1, 11, 7, 9, 6, 4, 1, 14, 4, 6, 2, 6, 0};
    private static final short      t10HB[]   = {1, 2, 10, 23, 35, 30, 12, 17, 3, 3, 8, 12, 18, 21, 12, 7, 11, 9, 15, 21, 32, 40, 19, 6, 14, 13, 22, 34, 46, 23, 18, 7, 20, 19, 33, 47, 27, 22, 9, 3, 31, 22, 41, 26, 21, 20, 5, 3, 14, 13, 10, 11, 16, 6, 5, 1, 9, 8, 7, 8, 4 , 4, 2, 0};
    private static final short      t11HB[]   = {3, 4, 10, 24, 34, 33, 21, 15, 5, 3, 4, 10, 32, 17, 11, 10, 11, 7, 13, 18, 30, 31, 20, 5, 25, 11, 19, 59, 27, 18, 12, 5, 35, 33, 31, 58, 30, 16, 7, 5, 28, 26, 32, 19, 17, 15, 8, 14, 14, 12, 9, 13, 14, 9, 4, 1, 11, 4, 6, 6, 6, 3, 2, 0};
    private static final short      t12HB[]   = {9, 6, 16, 33, 41, 39, 38, 26, 7, 5, 6, 9, 23, 16, 26, 11, 17, 7, 11, 14, 21, 30, 10, 7, 17, 10, 15, 12, 18, 28, 14, 5, 32, 13, 22, 19, 18, 16, 9, 5, 40, 17, 31, 29, 17, 13, 4, 2, 27, 12, 11, 15, 10, 7, 4, 1, 27, 12, 8, 12 , 6, 3, 1, 0};
    private static final short      t13HB[]   = {1, 5, 14, 21, 34, 51, 46, 71, 42, 52, 68, 52, 67, 44, 43, 19, 3, 4, 12, 19, 31, 26, 44, 33, 31, 24, 32, 24, 31, 35, 22, 14, 15, 13, 23, 36, 59, 49, 77, 65, 29, 40, 30, 40, 27, 33, 42, 16, 22,
    20, 37, 61, 56, 79, 73, 64, 43, 76, 56, 37, 26, 31, 25, 14, 35, 16, 60, 57, 97, 75, 114, 91, 54, 73, 55, 41, 48, 53, 23, 24, 58, 27, 50, 96, 76, 70, 93, 84, 77, 58, 79, 29, 74, 49, 41, 17, 47,
    45, 78, 74, 115, 94, 90, 79, 69, 83, 71, 50, 59, 38, 36, 15, 72, 34, 56, 95, 92, 85, 91, 90, 86, 73, 77, 65, 51, 44, 43, 42, 43, 20, 30, 44, 55, 78, 72, 87, 78, 61, 46, 54, 37, 30, 20, 16, 53,
    25, 41, 37, 44, 59, 54, 81, 66, 76, 57, 54, 37, 18, 39, 11, 35, 33, 31, 57, 42, 82, 72, 80, 47, 58, 55, 21, 22, 26, 38, 22, 53, 25, 23, 38, 70, 60, 51, 36, 55, 26, 34, 23, 27, 14, 9, 7, 34, 32,
    28, 39, 49, 75, 30, 52, 48, 40, 52, 28, 18, 17, 9, 5, 45, 21, 34, 64, 56, 50, 49, 45, 31, 19, 12, 15, 10, 7, 6, 3, 48, 23, 20, 39, 36, 35, 53, 21, 16, 23, 13, 10, 6, 1, 4, 2, 16, 15, 17, 27, 25,
    20, 29, 11, 17, 12, 16, 8, 1, 1, 0, 1};
    private static final short      t15HB[]   = {7, 12, 18, 53, 47, 76, 124, 108, 89, 123, 108, 119, 107, 81, 122, 63, 13, 5, 16, 27, 46, 36, 61, 51, 42, 70, 52, 83, 65, 41, 59, 36, 19, 17, 15, 24, 41, 34, 59, 48, 40, 64, 50, 78, 62, 80, 56,
    33, 29, 28, 25, 43, 39, 63, 55, 93, 76, 59, 93, 72, 54, 75, 50, 29, 52, 22, 42, 40, 67, 57, 95, 79, 72, 57, 89, 69, 49, 66, 46, 27, 77, 37, 35, 66, 58, 52, 91, 74, 62, 48, 79, 63, 90, 62, 40, 38,
    125, 32, 60, 56, 50, 92, 78, 65, 55, 87, 71, 51, 73, 51, 70, 30, 109, 53, 49, 94, 88, 75, 66, 122, 91, 73, 56, 42, 64, 44, 21, 25, 90, 43, 41, 77, 73, 63, 56, 92, 77, 66, 47, 67, 48, 53, 36, 20,
    71, 34, 67, 60, 58, 49, 88, 76, 67, 106, 71, 54, 38, 39, 23, 15, 109, 53, 51, 47, 90, 82, 58, 57, 48, 72, 57, 41, 23, 27, 62, 9, 86, 42, 40, 37, 70, 64, 52, 43, 70, 55, 42, 25, 29, 18, 11, 11,
    118, 68, 30, 55, 50, 46, 74, 65, 49, 39, 24, 16, 22, 13, 14, 7, 91, 44, 39, 38, 34, 63, 52, 45, 31, 52, 28, 19, 14, 8, 9, 3, 123, 60, 58, 53, 47, 43, 32, 22, 37, 24, 17, 12, 15, 10, 2, 1, 71,
    37, 34, 30, 28, 20, 17, 26, 21, 16, 10, 6, 8, 6, 2, 0};
    private static final short      t16HB[]   = {1, 5, 14, 44, 74, 63, 110, 93, 172, 149, 138, 242, 225, 195, 376, 17, 3, 4, 12, 20, 35, 62, 53, 47, 83, 75, 68, 119, 201, 107, 207, 9, 15, 13, 23, 38, 67, 58, 103, 90, 161, 72, 127, 117,
    110, 209, 206, 16, 45, 21, 39, 69, 64, 114, 99, 87, 158, 140, 252, 212, 199, 387, 365, 26, 75, 36, 68, 65, 115, 101, 179, 164, 155, 264, 246, 226, 395, 382, 362, 9, 66, 30, 59, 56, 102,
    185, 173, 265, 142, 253, 232, 400, 388, 378, 445, 16, 111, 54, 52, 100, 184, 178, 160, 133, 257, 244, 228, 217, 385, 366, 715, 10, 98, 48, 91, 88, 165, 157, 148, 261, 248, 407, 397, 372,
    380, 889, 884, 8, 85, 84, 81, 159, 156, 143, 260, 249, 427, 401, 392, 383, 727, 713, 708, 7, 154, 76, 73, 141, 131, 256, 245, 426, 406, 394, 384, 735, 359, 710, 352, 11, 139, 129, 67, 125,
    247, 233, 229, 219, 393, 743, 737, 720, 885, 882, 439, 4, 243, 120, 118, 115, 227, 223, 396, 746, 742, 736, 721, 712, 706, 223, 436, 6, 202, 224, 222, 218, 216, 389, 386, 381, 364, 888,
    443, 707, 440, 437, 1728, 4, 747, 211, 210, 208, 370, 379, 734, 723, 714, 1735, 883, 877, 876, 3459, 865, 2, 377, 369, 102, 187, 726, 722, 358, 711, 709, 866, 1734, 871, 3458, 870, 434,
    0, 12, 10, 7, 11, 10, 17, 11, 9, 13, 12, 10, 7, 5, 3, 1, 3};
    private static final short      t24HB[]   = {15, 13, 46, 80, 146, 262, 248, 434, 426, 669, 653, 649, 621, 517, 1032, 88, 14, 12, 21, 38, 71, 130, 122, 216, 209, 198, 327, 345, 319, 297, 279, 42, 47, 22, 41, 74, 68, 128, 120, 221,
    207, 194, 182, 340, 315, 295, 541, 18, 81, 39, 75, 70, 134, 125, 116, 220, 204, 190, 178, 325, 311, 293, 271, 16, 147, 72, 69, 135, 127, 118, 112, 210, 200, 188, 352, 323, 306, 285,
    540, 14, 263, 66, 129, 126, 119, 114, 214, 202, 192, 180, 341, 317, 301, 281, 262, 12, 249, 123, 121, 117, 113, 215, 206, 195, 185, 347, 330, 308, 291, 272, 520, 10, 435, 115, 111,
    109, 211, 203, 196, 187, 353, 332, 313, 298, 283, 531, 381, 17, 427, 212, 208, 205, 201, 193, 186, 177, 169, 320, 303, 286, 268, 514, 377, 16, 335, 199, 197, 191, 189, 181, 174, 333,
    321, 305, 289, 275, 521, 379, 371, 11, 668, 184, 183, 179, 175, 344, 331, 314, 304, 290, 277, 530, 383, 373, 366, 10, 652, 346, 171, 168, 164, 318, 309, 299, 287, 276, 263, 513, 375,
    368, 362, 6, 648, 322, 316, 312, 307, 302, 292, 284, 269, 261, 512, 376, 370, 364, 359, 4, 620, 300, 296, 294, 288, 282, 273, 266, 515, 380, 374, 369, 365, 361, 357, 2, 1033, 280, 278,
    274, 267, 264, 259, 382, 378, 372, 367, 363, 360, 358, 356, 0, 43, 20, 19, 17, 15, 13, 11, 9, 7, 6, 4, 7, 5, 3, 1, 3};
    private static final short      t32HB[]   = {1, 5, 4, 5, 6, 5, 4, 4, 7, 3, 6, 0, 7, 2, 3, 1};
    private static final short      t33HB[]   = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    
    
    private static final short      t0l[]  = {0, 0};
    private static final short      t1l[]  = {1, 3, 2, 3};
    private static final short      t2l[]  = {1, 3, 6, 3, 3, 5, 5, 5, 6};
    private static final short      t3l[]  = {2, 2, 6, 3, 2, 5, 5, 5, 6};
    private static final short      t5l[]  = {1, 3, 6, 7, 3, 3, 6, 7, 6, 6, 7, 8, 7, 6, 7, 8};
    private static final short      t6l[]  = {3, 3, 5, 7, 3, 2, 4, 5, 4, 4, 5, 6, 6, 5, 6, 7};
    private static final short      t7l[]  = {1, 3, 6, 8, 8, 9, 3, 4, 6, 7, 7, 8, 6, 5, 7, 8, 8, 9, 7, 7, 8, 9, 9, 9, 7, 7, 8, 9, 9, 10, 8, 8, 9, 10, 10, 10};
    private static final short      t8l[]  = {2, 3, 6, 8, 8, 9, 3, 2, 4, 8, 8, 8, 6, 4, 6, 8, 8, 9, 8, 8, 8, 9, 9, 10, 8, 7, 8, 9, 10, 10, 9, 8, 9, 9, 11, 11};
    private static final short      t9l[]  = {3, 3, 5, 6, 8, 9, 3, 3, 4, 5, 6, 8, 4, 4, 5, 6, 7, 8, 6, 5, 6, 7, 7, 8, 7, 6, 7, 7, 8, 9, 8, 7, 8, 8, 9, 9};
    private static final short      t10l[]  = {1, 3, 6, 8, 9, 9, 9, 10, 3, 4, 6, 7, 8, 9, 8, 8, 6, 6, 7, 8, 9, 10, 9, 9, 7, 7, 8, 9, 10, 10, 9, 10, 8, 8, 9, 10, 10, 10, 10, 10, 9, 9, 10, 10, 11, 11, 10, 11, 8, 8, 9, 10, 10, 10, 11, 11, 9, 8, 9, 10, 10, 11, 11, 11};
    private static final short      t11l[]  = {2, 3, 5, 7, 8, 9, 8, 9, 3, 3, 4, 6, 8, 8, 7, 8, 5, 5, 6, 7, 8, 9, 8, 8, 7, 6, 7, 9, 8, 10, 8, 9, 8, 8, 8, 9, 9, 10, 9, 10, 8, 8, 9, 10, 10, 11, 10, 11, 8, 7, 7, 8, 9, 10, 10, 10, 8, 7, 8, 9, 10, 10, 10, 10};
    private static final short      t12l[]  = {4, 3, 5, 7, 8, 9, 9, 9, 3, 3, 4, 5, 7, 7, 8, 8, 5, 4, 5, 6, 7, 8, 7, 8, 6, 5, 6, 6, 7, 8, 8, 8, 7, 6, 7, 7, 8, 8, 8, 9, 8, 7, 8, 8, 8, 9, 8, 9, 8, 7, 7, 8, 8, 9, 9, 10, 9, 8, 8, 9, 9, 9, 9, 10};
    private static final short      t13l[]  = {1, 4, 6, 7, 8, 9, 9, 10, 9, 10, 11, 11, 12, 12, 13, 13, 3, 4, 6, 7, 8, 8, 9, 9, 9, 9, 10, 10, 11, 12, 12, 12, 6, 6, 7, 8, 9, 9, 10, 10, 9, 10, 10, 11, 11, 12, 13, 13, 7, 7, 8, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 13, 13,
    8, 7, 9, 9, 10, 10, 11, 11, 10, 11, 11, 12, 12, 13, 13, 14, 9, 8, 9, 10, 10, 10, 11, 11, 11, 11, 12, 11, 13, 13, 14, 14, 9, 9, 10, 10, 11, 11, 11, 11, 11, 12, 12, 12, 13, 13, 14, 14, 10, 9, 10, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 14, 16, 16, 9, 8, 9, 10,
    10, 11, 11, 12, 12, 12, 12, 13, 13, 14, 15, 15, 10, 9, 10, 10, 11, 11, 11, 13, 12, 13, 13, 14, 14, 14, 16, 15, 10, 10, 10, 11, 11, 12, 12, 13, 12, 13, 14, 13, 14, 15, 16, 17, 11, 10, 10, 11, 12, 12, 12, 12, 13, 13, 13, 14, 15, 15, 15, 16, 11, 11, 11, 12, 12,
    13, 12, 13, 14, 14, 15, 15, 15, 16, 16, 16, 12, 11, 12, 13, 13, 13, 14, 14, 14, 14, 14, 15, 16, 15, 16, 16, 13, 12, 12, 13, 13, 13, 15, 14, 14, 17, 15, 15, 15, 17, 16, 16, 12, 12, 13, 14, 14, 14, 15, 14, 15, 15, 16, 16, 19, 18, 19, 16};
    private static final short      t15l[]  = {3, 4, 5, 7, 7, 8, 9, 9, 9, 10, 10, 11, 11, 11, 12, 13, 4, 3, 5, 6, 7, 7, 8, 8, 8, 9, 9, 10, 10, 10, 11, 11, 5, 5, 5, 6, 7, 7, 8, 8, 8, 9, 9, 10, 10, 11, 11, 11, 6, 6, 6, 7, 7, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 7, 6, 7,
    7, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 11, 8, 7, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 11, 11, 11, 12, 9, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 12, 12, 9, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 11, 11, 11, 12, 9, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11,
    12, 12, 12, 9, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 10, 9, 9, 9, 10, 10, 10, 10, 10, 11, 11, 11, 11, 12, 13, 12, 10, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 13, 11, 10, 9, 10, 10, 10, 11, 11, 11, 11, 11, 11, 12, 12, 13, 13,
    11, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 12, 13, 13, 12, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 12, 13, 12, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 13, 13, 13, 13};
    private static final short      t16l[]  = {1, 4, 6, 8, 9, 9, 10, 10, 11, 11, 11, 12, 12, 12, 13, 9, 3, 4, 6, 7, 8, 9, 9, 9, 10, 10, 10, 11, 12, 11, 12, 8, 6, 6, 7, 8, 9, 9, 10, 10, 11, 10, 11, 11, 11, 12, 12, 9, 8, 7, 8, 9, 9, 10, 10, 10, 11, 11, 12, 12, 12, 13, 13,
    10, 9, 8, 9, 9, 10, 10, 11, 11, 11, 12, 12, 12, 13, 13, 13, 9, 9, 8, 9, 9, 10, 11, 11, 12, 11, 12, 12, 13, 13, 13, 14, 10, 10, 9, 9, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 14, 10, 10, 9, 10, 10, 11, 11, 11, 12, 12, 13, 13, 13, 13, 15, 15, 10, 10, 10,
    10, 11, 11, 11, 12, 12, 13, 13, 13, 13, 14, 14, 14, 10, 11, 10, 10, 11, 11, 12, 12, 13, 13, 13, 13, 14, 13, 14, 13, 11, 11, 11, 10, 11, 12, 12, 12, 12, 13, 14, 14, 14, 15, 15, 14, 10, 12, 11, 11, 11, 12, 12, 13, 14, 14, 14, 14, 14, 14, 13, 14, 11, 12, 12,
    12, 12, 12, 13, 13, 13, 13, 15, 14, 14, 14, 14, 16, 11, 14, 12, 12, 12, 13, 13, 14, 14, 14, 16, 15, 15, 15, 17, 15, 11, 13, 13, 11, 12, 14, 14, 13, 14, 14, 15, 16, 15, 17, 15, 14, 11, 9, 8, 8, 9, 9, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 8};
    private static final short      t24l[]  = {4, 4, 6, 7, 8, 9, 9, 10, 10, 11, 11, 11, 11, 11, 12, 9, 4, 4, 5, 6, 7, 8, 8, 9, 9, 9, 10, 10, 10, 10, 10, 8, 6, 5, 6, 7, 7, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 7, 7, 6, 7, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 7, 8, 7, 7, 8,
    8, 8, 8, 9, 9, 9, 10, 10, 10, 10, 11, 7, 9, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 7, 9, 8, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 11, 7, 10, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 10, 10, 11, 11, 8, 10, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 11, 11,
    8, 10, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 11, 11, 11, 8, 11, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 8, 11, 10, 9, 9, 9, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 8, 11, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 8, 11, 10, 10,
    10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 8, 12, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 8, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 4};
    private static final short      t32l[]  = {1, 4, 4, 5, 4, 6, 5, 6, 4, 5, 5, 6, 5, 6, 6, 6};
    private static final short      t33l[]  = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
    
    
    private static final short      [] xlen = { 0, 2, 3, 3, 0, 4, 4, 6, 6, 6, 8, 8, 8, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 1, 1};               /*max. x-index+			      	*/
    private static final short      [] ylen = { 0, 2, 3, 3, 0, 4, 4, 6, 6, 6, 8, 8, 8, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};               /*max. y-index+				*/
    private static final short      [] linbits = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 1, 2, 3, 4, 6, 8, 10, 13, 4, 5, 6, 7, 8, 9, 11, 13, 0, 0};            /*number of linbits			*/
    private static final short      [] linmax = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 7, 15, 63, 255, 1023, 8191, 15, 31, 63, 127, 255, 511, 2047, 8191, 0, 0 };             /*max number to be stored in linbits	*/
    private static final short      [][] table = { t0HB, t1HB, t2HB, t3HB, null, t5HB, t6HB, t7HB, t8HB, t9HB, t10HB, t11HB, t12HB,  t13HB, null, t15HB, t16HB, t16HB, t16HB, t16HB, t16HB, t16HB, t16HB, t16HB, t24HB, t24HB, t24HB, t24HB, t24HB, t24HB, t24HB, t24HB, t32HB, t33HB};	/*pointer to array[xlen][ylen]		*/
    private static final short      [][] hlen = { t0l, t1l, t2l, t3l, null, t5l, t6l, t7l, t8l, t9l, t10l, t11l, t12l, t13l, null, t15l, t16l, t16l, t16l, t16l, t16l, t16l, t16l, t16l, t24l, t24l, t24l, t24l, t24l, t24l, t24l, t24l, t32l, t33l};         /*pointer to array[xlen][ylen]		*/
    
    
    private int [][][] sfBandIndex = CommonMethods.sfBandIndex;
//    {
//        { /* Table B.2.b: 22.05 kHz */
//            {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576},
//            {0,4,8,12,18,24,32,42,56,74,100,132,174,192}
//        },
//        { /* Table B.2.c: 24 kHz */
//            {0,6,12,18,24,30,36,44,54,66,80,96,114,136,162,194,232,278,330,394,464,540,576},
//            {0,4,8,12,18,26,36,48,62,80,104,136,180,192}
//        },
//        { /* Table B.2.a: 16 kHz */
//            {0,6,12,18,24,30,36,44,45,66,80,96,116,140,168,200,238,248,336,396,464,522,576},
//            {0,4,8,12,18,26,36,48,62,80,104,134,174,192}
//        },
//        { /* Table B.8.b: 44.1 kHz */
//            {0,4,8,12,16,20,24,30,36,44,52,62,74,90,110,134,162,196,238,288,342,418,576},
//            {0,4,8,12,16,22,30,40,52,66,84,106,136,192}
//        },
//        { /* Table B.8.c: 48 kHz */
//            {0,4,8,12,16,20,24,30,36,42,50,60,72,88,106,128,156,190,230,276,330,384,576},
//            {0,4,8,12,16,22,28,38,50,64,80,100,126,192}
//        },
//        { /* Table B.8.a: 32 kHz */
//            {0,4,8,12,16,20,24,30,36,44,54,66,82,102,126,156,194,240,296,364,448,550,576},
//            {0,4,8,12,16,22,30,42,58,78,104,138,180,192}
//        }
//    };
    
    private int [][] subdv_table = {
        {0, 0}, /* 0 bands */
        {0, 0}, /* 1 bands */
        {0, 0}, /* 2 bands */
        {0, 0}, /* 3 bands */
        {0, 0}, /* 4 bands */
        {0, 1}, /* 5 bands */
        {1, 1}, /* 6 bands */
        {1, 1}, /* 7 bands */
        {1, 2}, /* 8 bands */
        {2, 2}, /* 9 bands */
        {2, 3}, /* 10 bands */
        {2, 3}, /* 11 bands */
        {3, 4}, /* 12 bands */
        {3, 4}, /* 13 bands */
        {3, 4}, /* 14 bands */
        {4, 5}, /* 15 bands */
        {4, 5}, /* 16 bands */
        {4, 6}, /* 17 bands */
        {5, 6}, /* 18 bands */
        {5, 6}, /* 19 bands */
        {5, 7}, /* 20 bands */
        {6, 7}, /* 21 bands */
        {6, 7}, /* 22 bands */
    };
    
    private int [] scalefac_band_long; //= sfBandIndex[3][0];      // long block
    private int [] scalefac_band_short;// = sfBandIndex[3][1];     // short block
    
    
    private static final int GR_MAX = 2;
    
    //private int [] ix;
    private MainDataEnc mn;
    
    private int max_bits;
    
    public HuffmanEnc(MainDataEnc mn){
        this.mn = mn;
        
    }
    
    /*  iteration_loop()                                                    */
    public byte[] iteration_loop(SideInfoEnc side_info, FrameDataQuantized fd, int mean_bits) {
        int channels = fd.getChannels();
        //int part2_len;
        EChannel cod_info;
        int main_data_begin;
        
        int max_bits;
        
        
        int enc[][][] = fd.ix;
        
        SideInfo side_info_dec = fd.si;
        
       /* switch(fd.getFs()){
            case 22050: scalefac_band_long  = sfBandIndex[0][0]; scalefac_band_short = sfBandIndex[0][1]; break;
            case 24000: scalefac_band_long  = sfBandIndex[1][0]; scalefac_band_short = sfBandIndex[1][1]; break;
            case 16000: scalefac_band_long  = sfBandIndex[2][0]; scalefac_band_short = sfBandIndex[2][1]; break;
            case 44100: scalefac_band_long  = sfBandIndex[3][0]; scalefac_band_short = sfBandIndex[3][1]; break;
            case 48000: scalefac_band_long  = sfBandIndex[4][0]; scalefac_band_short = sfBandIndex[4][1]; break;
            case 32000: scalefac_band_long  = sfBandIndex[5][0]; scalefac_band_short = sfBandIndex[5][1]; break;
        }*/
        
        int freq_index = fd.get_fs_index();
        scalefac_band_long = sfBandIndex[freq_index][0];
        scalefac_band_short = sfBandIndex[freq_index][1];
        
        // part2_len = 0;
        for (int ch = 0; ch < channels; ch ++)
            for (int i = 0; i < 4 ; i++){
            side_info.scfsi[ch][i] = fd.si.scfsi[ch][i];
            }
        
        //  ResvFrameBegin( side_info, mean_bits, 418*8 );
        for( int gr = 0; gr < GR_MAX; gr++ ) {
            for( int ch = 0; ch < channels; ch++ ) {
                
                cod_info = side_info.gr[gr].ch[ch];
                Channel gr_info_dec = side_info_dec.gr[gr].ch[ch];
                
//                System.out.println("--------------------");
//                System.out.println("ch: " + ch);
//                System.out.println("ch: " + ch);
//
                /* calculation of number of available bit( per granule ) */
                max_bits = mean_bits / channels;
                
                cod_info.part2_3_length    = 0;
                cod_info.setBigValues(0);
                cod_info.setCount1(0);
                cod_info.scalefac_compress = gr_info_dec.scalefac_compress;
                cod_info.table_select[0]   = 0;
                cod_info.table_select[1]   = 0;
                cod_info.table_select[2]   = 0;
                cod_info.region0_count     = 0;
                cod_info.region1_count     = 0;
                cod_info.part2_length      = 0;
                cod_info.window_switching_flag = gr_info_dec.window_switching_flag;
                cod_info.preflag           = gr_info_dec.preflag;
                cod_info.scalefac_scale    = gr_info_dec.scalefac_scale;
                cod_info.block_type        = gr_info_dec.block_type;
                cod_info.subblock_gain[0]   = gr_info_dec.subblock_gain[0];
                cod_info.subblock_gain[1]   = gr_info_dec.subblock_gain[1];
                cod_info.subblock_gain[2]   = gr_info_dec.subblock_gain[2];
                
                
                cod_info.mixed_block_flag  = gr_info_dec.mixed_block_flag;
                
                // if(ch==0){
                this.scalefac0L[gr][ch] = fd.scalefac0L[gr][ch];
                this.scalefac0S[gr][ch] = fd.scalefac0S[gr][ch];
                /*} else{
                    this.scalefac1L = fd.scalefac1L[ch][ch];
                    this.scalefac1S = fd.scalefac1S[ch][ch];
                }*/
                // bigvalues_region = FREQUENCY_LINES;
                //cod_info.quantizerStepSize = 0.0;
                cod_info.count1table_select = 0;
                int ix_abs[] = new int [FREQUENCY_LINES];
                int [] ix = enc[gr][ch];
                for(int i = 0; i < FREQUENCY_LINES; i++)
                    ix_abs[i] = Math.abs(ix[i]);
                outer_loop(max_bits, ix_abs, gr, ch, side_info );
                
                //System.out.println("huff enc -> back from outer loop");
                //ResvAdjust(cod_info, mean_bits, channels );
//                System.out.println("biv: " + bigvalues_region);
//                System.out.println("count1_region: " + count1_region);
                cod_info.global_gain = gr_info_dec.global_gain;
                
                
            } /* for ch */
        } /* for ch */
        
        //ResvFrameEnd(side_info, mean_bits, channels);
        
        
        for(int gr = 0; gr < GR_MAX; gr++ ){
            for(int ch = 0; ch < channels; ch++ ) {
                int [] ix = enc[gr][ch];
                
//                for (int i = 0; i < 576; i++)
//                    System.out.println(i + ") " + ix[i]);
//                System.out.println("--------------------");
//                System.out.println("ch: " + ch);
//                System.out.println("ch: " + ch);
                cod_info = side_info.gr[gr].ch[ch];
                write_scalefactors(side_info, ch, gr);
                int bits = Huffmancodebits( ix, cod_info );
                cod_info.part2_3_length = cod_info.part2_length + bits;
            }
        }
        
        return mn.format_main_data();
    }
    
    
    
    /**  Function: The outer iteration loop controls the masking conditions
     *  of all scalefactorbands. It computes the best scalefac and
     *  global gain. This module calls the inner iteration loop             */
    private void outer_loop( int max_bits,
            int ix[],    /* vector of frequency values  */
            int gr, int ch, SideInfoEnc side_info ) {
        
        int bits = 0;
        EChannel cod_info = side_info.gr[gr].ch[ch];
        
        cod_info.part2_length = part2_length(gr,ch,side_info);
        bits = inner_loop( ix, cod_info, max_bits );
        // cod_info.part2_3_length = cod_info.part2_length + bits;
        
    }
    
    
    
    private int inner_loop(int [] ix_abs, EChannel cod_info, int max_bits){
        int c1bits, bvbits, bits = 0;
        
        
        
        bits = calc_runlen( ix_abs, cod_info );                        /* rzero,count1,big_values*/
        
//             if( cod_info.big_values < 288 )
//               bits = count1_bitcount( ix, cod_info );         /* count1_table selection*/
        
        // subdivide( cod_info );                              /* bigvalues sfb division */
        
        bigv_tab_select(ix_abs,cod_info);                       /* codebook selection*/
        
        //bits += bigv_bitcount( ix, cod_info );              /* bit count */                           //<--------
        
        return bits;
    }
    
    
    
    /** calculates the number of bits needed to encode the scalefacs in the
     * main data block                                                         */
    
    private int part2_length(int gr, int ch,   SideInfoEnc si) {
        int slen1, slen2, bits;
        EChannel gi = si.gr[gr].ch[ch];
        
        bits = 0;
        
        slen1 = slen1_tab[ gi.scalefac_compress ];
        slen2 = slen2_tab[ gi.scalefac_compress ];
        
        if ( (gi.window_switching_flag == 1) && (gi.block_type == 2) ) {
            if ( gi.mixed_block_flag != 0 ) {
                bits += (8 * slen1) + (9 * slen1) + (18 * slen2);
            } else {
                bits += (18 * slen1) + (18 * slen2);
            }
        } else {
            if ( gr != 0 || (si.scfsi[ch][0] == 0 ) )
                bits += (6 * slen1);
            
            if ( gr != 0 || (si.scfsi[ch][1] == 0 ) )
                bits += (5 * slen1);
            
            if ( gr != 0 || (si.scfsi[ch][2] == 0 ) )
                bits += (5 * slen2);
            
            if ( gr != 0 || (si.scfsi[ch][3] == 0 ) )
                bits += (5 * slen2);
        }
        
        return bits;
    }
    
    
    final static short slen1_tab[] ={0,0,0,0,3,1,1,1,2,2,2,3,3,3,4,4};
    final static short slen2_tab[] ={0,1,2,3,0,1,2,3,1,2,3,1,2,3,2,3};
    
    
    private static int[][][] scalefac0L= new int[2][2][23];
    private static int[][][][] scalefac0S= new int[2][2][3][13];

    
    private final void write_scalefactors(SideInfoEnc si, int ch, int gr) {
        
        EChannel gr_info = (si.gr[gr].ch[ch]);
        int scale_comp = gr_info.scalefac_compress;
        int length0 = slen1_tab[scale_comp];
        int length1 = slen2_tab[scale_comp];
        int l[] = null;
        int s[][] = null;
        //if(ch==0){
        l = scalefac0L[gr][ch];
        s = scalefac0S[gr][ch];
        /*} else{
            l = scalefac1L;
            s = scalefac1S;
        }*/
        
        // System.out.println("\n ch: "+ch+", ch: "+ch);
        //System.out.println("scalefactors: " + );
        if ((gr_info.window_switching_flag != 0) && (gr_info.block_type == 2)) {
            
            
            if ((gr_info.mixed_block_flag) != 0) {
                
                // MIXED
                for (int sfb = 0; sfb < 8; sfb++) {
                    mn.add_entry(l[sfb], slen1_tab[gr_info.scalefac_compress]);
                }
                for (int sfb = 3; sfb < 6; sfb++) {
                    for (int window = 0; window < 3; window++) {
                        mn.add_entry(s[window][sfb], slen1_tab[gr_info.scalefac_compress]);
                    
                    }
                }
                for (int sfb = 6; sfb < 12; sfb++) {
                    for (int window = 0; window < 3; window++) {
                    
                        mn.add_entry(s[window][sfb], slen2_tab[gr_info.scalefac_compress]);
                    
                    }
                }
            } else {
                // SHORT
                for(int sfb = 0; sfb< 6; sfb++)
                    for(int window = 0; window< 3; window++){
                    mn.add_entry(s[window][sfb], length0);
                    
                    }
                
                
                for(int sfb = 6; sfb< 12; sfb++)
                    for(int window = 0; window< 3; window++){
                    mn.add_entry(s[window][sfb], length1);
                    
                    }
            }
            
            // SHORT
        } else {
            // LONG types 0,1,3
            
            int si_t[] = si.scfsi[ch];
            if (gr == 0 || si_t[0] == 0) {
                for(int i = 0; i< 6; i++){
                    mn.add_entry(l[i], length0);
                }
            }
            if (gr == 0 || si_t[1] == 0) {
                for(int i = 6; i< 11; i++){
                    mn.add_entry(l[i], length0);
                }
            }
            if (gr == 0 || si_t[2] == 0) {
                for(int i = 11; i< 16; i++){
                    mn.add_entry(l[i], length1);
                }
            }
            if (gr == 0 || si_t[3] == 0) {
                for(int i = 16; i< 21; i++){
                    mn.add_entry(l[i], length1);
                }
            }
        }
    }
    
    
    
    private static int rzero = 0;
    
    /**
     *  Function: Calculation of rzero, count1, big_values
     * (Partitions ix into big values, quadruples and zeros).
     */
//    private void calc_runlen( int [] ix, EChannel cod_info ) {
//        int i;
//
//        cod_info.big_values = 0;
//        rzero = 0;
//
//        if ( cod_info.window_switching_flag != 0 && (cod_info.block_type == 2) ) {
//            /* short blocks */
//            cod_info.count1 = 0;
//            cod_info.big_values = 288;
//
//        } else {
//            for ( i = FREQUENCY_LINES; i > 1; i -= 2 )
//                if ( ix[i-1] == 0 && ix[i-2] == 0 )
//                    rzero++;
//                else
//                    break;
//
//            cod_info.count1 = 0 ;
//            for ( ; i > 3; i -= 4 )
//                if ( Math.abs(ix[i-1]) <= 1 && Math.abs(ix[i-2]) <= 1 && Math.abs(ix[i-3]) <= 1 && Math.abs(ix[i-4]) <= 1 )
//                    cod_info.count1++;
//                else
//                    break;
//
//
//            cod_info.big_values = i >> 1;
//        }
//    }
    
    
    private int calc_runlen( int [] ix_abs, EChannel cod_info) {
        int p = 0, bits = 0;
        
        int sum0 = 0, sum1 = 0;
        //      System.out.println("\n-----------");
        
        
        
        //ix_abs[i] = (ix[i] < 0)? -ix[i]: ix[i];
//        for(int i = 0; i < FREQUENCY_LINES; i++)
//            System.out.println(i+") " + ix[i]);
        
        bigvalues_region = FREQUENCY_LINES;
        zero_region      = FREQUENCY_LINES;
        if (cod_info.block_type != 2) {
            for (; zero_region>1; zero_region-= 2)
                if (ix_abs[zero_region-1] != 0)  break;
                else if (ix_abs[zero_region-2] != 0)  break;
            
            for (bigvalues_region=zero_region; bigvalues_region>3; bigvalues_region-= 4) {
                if (ix_abs[bigvalues_region-1] > 1)  break;
                else if (ix_abs[bigvalues_region-2] > 1)  break;
                else if (ix_abs[bigvalues_region-3] > 1)  break;
                else if (ix_abs[bigvalues_region-4] > 1)  break;
                
                p = 0;
                if (ix_abs[bigvalues_region-1] != 0)  {bits++; p |= 8;}
                if (ix_abs[bigvalues_region-2] != 0)  {bits++; p |= 4;}
                if (ix_abs[bigvalues_region-3] != 0)  {bits++; p |= 2;}
                if (ix_abs[bigvalues_region-4] != 0)  {bits++; p |= 1;}
                
                sum0 += hlen[32][p];
                sum1 += hlen[33][p];
            }
        }
        
        this.count1_region = zero_region - bigvalues_region;
        cod_info.setCount1((zero_region-bigvalues_region) / 4);// / 4;

        cod_info.setBigValues(bigvalues_region / 2);
        
        if (sum0 < sum1) {
            bits += sum0;
            cod_info.count1table_select = 0;
        } else {
            bits += sum1;
            cod_info.count1table_select = 1;
        }
        
        if (bigvalues_region != 0) {
            if (cod_info.window_switching_flag == 0) {
                int index0, index1;
                int scfb_anz = 0;
                
                /* Calculate scfb_anz */
                while (scalefac_band_long[scfb_anz] < bigvalues_region)
                    scfb_anz++;
                
                index0 = (cod_info.region0_count = subdv_table[scfb_anz][0]) + 1;
                index1 = (cod_info.region1_count = subdv_table[scfb_anz][1]) + 1;
                
                cod_info.address1 = scalefac_band_long[index0];
                cod_info.address2 = scalefac_band_long[index0 + index1];
                cod_info.address3 = bigvalues_region;
            } else {	/* long blocks */
                if ( (cod_info.block_type == 2) && (cod_info.mixed_block_flag == 0) ) {
                    cod_info.region0_count =  8;
                    cod_info.region1_count =  12;
                    cod_info.address1 = 36;
                } else {
                    cod_info.region0_count = 7;
                    cod_info.region1_count = 13;
                    cod_info.address1 = scalefac_band_long[ cod_info.region0_count + 1 ];
                }
                cod_info.address2 = bigvalues_region;
                cod_info.address3 = 0;
                
            }
        } else {	/* no big_values region */
            cod_info.region0_count = 0;
            cod_info.region1_count = 0;
            cod_info.address1 = 0;
            cod_info.address2 = 0;
            cod_info.address3 = 0;
        }
        
        return bits;
    }
    
    
    /** subdivides the bigvalue region which will use separate Huffman tables.  */
    private void subdivide(EChannel cod_info) {
        int scfb_anz = 0;
        
        if ( bigvalues_region == 0) {
            /* no big_values region */
            cod_info.region0_count = 0;
            cod_info.region1_count = 0;
        } else {
            
            // System.out.println("bigv in subdivide: " + bigvalues_region);
            if ( cod_info.window_switching_flag == 0 ) {
                      
                int index0, index1;
                // int scfb_anz = 0;
                
                /* Calculate scfb_anz */
                while (scalefac_band_long[scfb_anz] < bigvalues_region)
                    scfb_anz++;
                /*			assert (scfb_anz < 23); */
                
                index0 = (cod_info.region0_count = subdv_table[scfb_anz][0]) + 1;
                index1 = (cod_info.region1_count = subdv_table[scfb_anz][1]) + 1;
                
                cod_info.address1 = scalefac_band_long[index0];
                cod_info.address2 = scalefac_band_long[index0 + index1];
                cod_info.address3 = bigvalues_region;
            } else {
                if ( (cod_info.block_type == 2) && (cod_info.mixed_block_flag == 0) ) {
                    cod_info.region0_count =  8;
                    cod_info.region1_count =  12;
                    cod_info.address1 = 36;
                } else {
                    cod_info.region0_count = 7;
                    cod_info.region1_count = 13;
                    cod_info.address1 = scalefac_band_long[ cod_info.region0_count + 1 ];
                }
                cod_info.address2 = bigvalues_region;
                cod_info.address3 = 0;
                
            }
        }
    }
    
    
    /** Determines the number of bits to encode the quadruples.               */
    private int count1_bitcount(int [] ix, EChannel cod_info) {
        int p, i, k;
        int signbits;
        int sum0 = 0, sum1 = 0;
        int count1End = bigvalues_region + count1_region;
        
        for(i = bigvalues_region, k=0; k < count1End; i+=4, k++) {
            v = Math.abs(ix[i]);
            w = Math.abs(ix[i+1]);
            x = Math.abs(ix[i+2]);
            y = Math.abs(ix[i+3]);
            
            p = signbits = 0;
            if(v!=0) { signbits++; p |= 1; }
            if(w!=0) { signbits++; p |= 2; }
            if(x!=0) { signbits++; p |= 4; }
            if(y!=0) { signbits++; p |= 8; }
            
            sum0 += signbits;
            sum1 += signbits;
            
            sum0 += hlen[32][p];
            sum1 += hlen[33][p];
        }
        
        if(sum0 < sum1) {
            cod_info.count1table_select = 0;
            return sum0;
        } else {
            cod_info.count1table_select = 1;
            return sum1;
        }
    }
    
    
    /**   Function: Select huffman code tables for bigvalues regions */
    private void bigv_tab_select( int [] ix_abs, EChannel cod_info ) {
        cod_info.table_select[0] = 0;
        cod_info.table_select[1] = 0;
        cod_info.table_select[2] = 0;
        
        if ( cod_info.window_switching_flag != 0 && cod_info.block_type == 2 ) {
        /*
          Within each scalefactor band, data is given for successive
          time windows, beginning with window 0 and ending with window 2.
          Within each window, the quantized values are then arranged in
          order of increasing frequency...
         */
            int sfb, window, line, start, end, max1, max2;
            int region1Start;
            int pmax = 0;
            
            region1Start = 12;
            max1 = max2 = 0;
            
            for ( sfb = 0; sfb < 13; sfb++ ) {
                start = scalefac_band_short[ sfb ];
                end   = scalefac_band_short[ sfb+1 ];
                
                if ( start < region1Start )
                    pmax = max1;
                else
                    pmax = max2;
                
                for ( window = 0; window < 3; window++ )
                    for ( line = start; line < end; line += 2 ) {
                    
                    x = ix_abs[ (line * 3) + window ];
                    y = ix_abs[ ((line + 1) * 3) + window ];
                    pmax = pmax > x ? pmax : x;
                    pmax = pmax > y ? pmax : y;
                    }
                
                if ( start < region1Start )
                    max1 = pmax;
                else
                    max2 = pmax;
            }
            
            cod_info.table_select[0] = choose_table( max1 );
            cod_info.table_select[1] = choose_table( max2 );
        } else {
            
            if ( cod_info.address1 > 0 )
                cod_info.table_select[0] = choose_table( ix_abs, 0, cod_info.address1 );
            
            if ( cod_info.address2 > cod_info.address1 )
                cod_info.table_select[1] = choose_table( ix_abs, cod_info.address1, cod_info.address2 );
            
            if ( bigvalues_region > cod_info.address2 )
                cod_info.table_select[2] = choose_table( ix_abs, cod_info.address2, bigvalues_region );
            
        }
    }
    
    
    /**  Choose the Huffman table that will encode ix[begin..end] with
     *  the fewest bits in case of short window
     *  Note: This code contains knowledge about the sizes and characteristics
     *  of the Huffman tables as defined in the IS (Table B.7), and will not work
     *  with any arbitrary tables.
     */
    private int choose_table( int max ) {
        int  i, choice;
        
        if ( max == 0 )
            return 0;
        
        max = Math.abs( max );
        choice = 0;
        
        if ( max < 15 ) {
            choice = 1;  /* not 0 -- ht[0].xlen == 0 */
            while (xlen[choice] <= max)
                choice++;
        } else {
            max -= 15;

            choice = 15;
            while (linmax[choice] < max)
                choice++;
        }
        
        return choice;
    }
    
    
    /**  Choose the Huffman table that will encode ix[begin..end] with
     *  the fewest bits in case of long window
     *  Note: This code contains knowledge about the sizes and characteristics
     *  of the Huffman tables as defined in the IS (Table B.7), and will not work
     *  with any arbitrary tables.
     */
    
    //private int choose_table( int ix, long begin, long end )
    private int choose_table( int [] ix_abs, int begin, int end ) {
        int i, max;
        
        max = ix_max(ix_abs,begin,end);
        
        if(max == 0)
            return 0;
        
        int choice0 = 0, choice1 = 0;
        int sum0 = 0, sum1 = 1;
        
        if(max<15) {
            /* try tables with no linbits */
            
            for ( i = 0; i <= 14; i++){
                if ( xlen[i] > max ) {
                    choice0 = i;
                    break;
                }
            }
            
            sum0 = count_bit( ix_abs, begin, end, choice0 );
            
            switch ( choice0 ) {
                case 2:
                    sum1 = count_bit( ix_abs, begin, end, 3 );
                    if ( sum1 <= sum0 )
                        choice0 = 3;
                    break;
                    
                case 5:
                    sum1 = count_bit( ix_abs, begin, end, 6 );
                    if ( sum1 <= sum0 )
                        choice0 = 6;
                    break;
                    
                case 7:
                    sum1 = count_bit( ix_abs, begin, end, 8 );
                    if ( sum1 <= sum0 ) {
                        choice0 = 8;
                        sum0 = sum1;
                    }
                    sum1 = count_bit( ix_abs, begin, end, 9 );
                    if ( sum1 <= sum0 )
                        choice0 = 9;
                    break;
                    
                case 10:
                    sum1 = count_bit( ix_abs, begin, end, 11 );
                    if ( sum1 <= sum0 ) {
                        choice0 = 11;
                        sum0 = sum1;
                    }
                    sum1 = count_bit( ix_abs, begin, end, 12 );
                    if ( sum1 <= sum0 )
                        choice0 = 12;
                    break;
                    
                case 13:
                    sum1 = count_bit( ix_abs, begin, end, 15 );
                    if ( sum1 <= sum0 )
                        choice0 = 15;
                    break;
                default: break;
            }
        } else {
            /* try tables with linbits */
            max -= 15;
            
            choice0 = 15;
            while (linmax[choice0] < max)
                choice0++;
            
            choice1 = 24;
            while (linmax[choice1] < max)
                choice1++;
            sum0 = count_bit(ix_abs, begin, end, choice0);
            sum1 = count_bit(ix_abs, begin, end, choice1);
            if (sum1 < sum0)
                choice0 = choice1;
        }
        
        return choice0;
    }
    
    
    /** Function: Count the number of bits necessary to code the bigvalues region.  */
    private int bigv_bitcount(int [] ix, EChannel gi) {
        int bits = 0;
        
        if ( gi.window_switching_flag != 0 && gi.block_type == 2 ) {
        /*
          Within each scalefactor band, data is given for successive
          time windows, beginning with window 0 and ending with window 2.
          Within each window, the quantized values are then arranged in
          order of increasing frequency...
         */
            int sfb = 0, window, line, start, end;
            
            if ( gi.mixed_block_flag != 0 ) {
                int tableindex;
                
                if ( (tableindex = gi.table_select[0]) != 0 )
                    bits += count_bit( ix, 0, gi.address1, tableindex );
                sfb = 2;
            }
            
            for ( ; sfb < 13; sfb++ ) {
                int tableindex = 100;
                
                start = scalefac_band_short[ sfb ];
                end   = scalefac_band_short[ sfb+1 ];
                
                if ( start < 12 )
                    tableindex = gi.table_select[ 0 ];
                else
                    tableindex = gi.table_select[ 1 ];
                
                bits += count_bit_short(ix, start, end, tableindex);/*
                for ( line = start; line < end; line += 2 ) {
                    for ( window = 0; window < 3; window++ ){
                        x = (ix[line * 3 + window ]);
                        y = (ix[(line + 1) * 3 + window]);
                                                                     
                        // x = (i192_3[ line ][ window ]);
                        // y = (i192_3[ line + 1 ][ window]);
                                                                     
                        bits += HuffmanCode( tableindex, x, y );
                    }
                }*/
            }
        } else {
            
            int table;
            
            if( (table = gi.table_select[0] )>=0)  // region0
                bits += count_bit(ix, 0, gi.address1, table );
            if( (table = gi.table_select[1])>=0)  // region1
                bits += count_bit(ix, gi.address1, gi.address2, table );
            if( (table = gi.table_select[2])>=0)  // region2
                bits += count_bit(ix, gi.address2, gi.address3, table );
        }
        
        return bits;
    }
    
    
  
    
    
    /** Function: Count the number of bits necessary to code short frame. */
    int count_bit_short( int [] ix, int start, int end, int table )
    
    {
        int        i, sum;
        int        x,y;
        
        if(table < 0 || table > 34)
            return 0;
        
        sum = 0;
        
        int ylen    = this.ylen[table];
        int linbits = this.linbits[table];
        
        for ( int line = start; line < end; line += 2 ) {
            for ( int window = 0; window < 3; window++ ){
                x = Math.abs(ix[line * 3 + window ]);
                y = Math.abs(ix[(line + 1) * 3 + window]);
                
                if(table > 15){
                    
                    if(x > 14) {
                        x = 15;
                        sum += linbits;
                    }
                    
                    if(y > 14) {
                        y = 15;
                        sum += linbits;
                    }
                }
                
                sum += hlen[table][(x*ylen)+y];
                if(x!=0) sum++;
                if(y!=0) sum++;
            }
        }
        return sum;
    }
    
    
    /** Function: Count the number of bits necessary to code the subregion. */
    int count_bit( int [] ix_abs, int start, int end, int table )
    
    {
        int        sum = 0;
        int        x,y;
        
        if(table < 0 || table > 34)
            return 0;
        
        int ylen    = this.ylen[table];
        int linbits = this.linbits[table];
        
        if(table > 15) { // ESC-table is used
            for(int i = start; i < end; i += 2) {
                x = ix_abs[i];
                y = ix_abs[i+1];
                if(x > 14) {
                    x = 15;
                    sum += linbits;
                }
                
                if(y > 14) {
                    y = 15;
                    sum += linbits;
                }
                
                sum += hlen[table][(x*ylen)+y];
                
                if(x!=0) sum++;
                if(y!=0) sum++;
            }
        } else { /* No ESC-words */
            for(int i = start; i < end; i += 2) {
                x = ix_abs[i];
                y = ix_abs[i+1];
                
                sum  += hlen[table][(x*ylen)+y];
                
                if(x!=0) sum++;
                if(y!=0) sum++;
            }
        }
        
        return sum;
    }
    
//    int count_bit( int ix[], int start, int end, int table ) {
//        int i, sum;
//
//        sum = 0;
//        for ( i = start; i < end; i += 2 ) {
//            sum += HuffmanCode( table, ix[i], ix[i+1] );
//        }
//        return sum;
//    }
    
    
    /**  Function: Calculate the maximum of ix from 0 to 575                  */
    private int ix_max( int [] ix_abs, int begin, int end ) {
        int i;
        int x;
        int max = 0;
        
        for( i = begin; i < end; i++ ) {
            x = ix_abs[i];
            if( x > max )
                max = x;
        }
        
        return max;
    }
    
    
    
    
    /*
      Note the discussion of huffmancodebits() on pages 28
      and 29 of the IS, as well as the definitions of the side
      information on pages 26 and 27.
     */
    private int Huffmancodebits( int [] ix, EChannel gi ) {
        int region1Start;
        int region2Start;
        int count1End;
        int bits, stuffingBits;
        int bvbits, c1bits, tablezeros, r0, r1, r2;//, rt, pr;
        int bitsWritten = 0;
        int idx = 0;
        tablezeros = 0;
        r0 = r1 = r2 = 0;
        
        int bigv = gi.big_values * 2;
        int count1 = gi.count1 * 4;
        
        
        /* 1: Write the bigvalues */
        
        if ( bigv!= 0 ) {
            if ( (gi.mixed_block_flag) == 0 && gi.window_switching_flag != 0 && (gi.block_type == 2) ) { /* Three short blocks */
                // if ( (gi.mixed_block_flag) == 0 && (gi.block_type == 2) ) { /* Three short blocks */
                /*
                Within each scalefactor band, data is given for successive
                time windows, beginning with window 0 and ending with window 2.
                Within each window, the quantized values are then arranged in
                order of increasing frequency...
                 */
                
                int sfb, window, line, start, end;
                
                //int [] scalefac = scalefac_band_short;        //da modificare nel caso si convertano mp3 con fs diversi
                
                region1Start = 12;
                region2Start = 576;
                
                for ( sfb = 0; sfb < 13; sfb++ ) {
                    int tableindex = 100;
                    start = scalefac_band_short[ sfb ];
                    end   = scalefac_band_short[ sfb+1 ];
                    
                    if ( start < region1Start )
                        tableindex = gi.table_select[ 0 ];
                    else
                        tableindex = gi.table_select[ 1 ];
                    
                    for ( window = 0; window < 3; window++ )
                        for ( line = start; line < end; line += 2 ) {
                        x = ix[ line * 3 + window ];
                        y = ix[ (line + 1) * 3 + window ];
                        
                        bits = HuffmanCode( tableindex, x, y );
                        mn.add_entry( code, cbits );
                        //        System.out.println((cont++)+") code: "+code+", ext: "+ext);
                        mn.add_entry( ext, xbits );
                        
                        bitsWritten += bits;
                        }
                    
                }
            } else
                if ( gi.mixed_block_flag!= 0 && gi.block_type == 2 ) {  /* Mixed blocks long, short */
                int sfb, window, line, start, end;
                int tableindex;
                //scalefac_band_long;
                
                /* Write the long block region */
                tableindex = gi.table_select[0];
                if ( tableindex != 0 )
                    for (int  i = 0; i < 36; i += 2 ) {
                    x = ix[i];
                    y = ix[i + 1];
                    bits = HuffmanCode( tableindex, x, y );
                    mn.add_entry( code, cbits );
                    mn.add_entry( ext, xbits );
                    bitsWritten += bits;
                    //      System.out.println((cont++)+") code: "+code+", ext: "+ext);
                    }
                /* Write the short block region */
                tableindex = gi.table_select[ 1 ];
                
                for ( sfb = 3; sfb < 13; sfb++ ) {
                    start = scalefac_band_long[ sfb ];
                    end   = scalefac_band_long[ sfb+1 ];
                    
                    for ( window = 0; window < 3; window++ )
                        for ( line = start; line < end; line += 2 ) {
                        x = ix[ line * 3 + window ];
                        y = ix[ (line + 1) * 3 + window ];
                        bits = HuffmanCode( tableindex, x, y );
                        mn.add_entry( code, cbits );
                        mn.add_entry( ext, xbits );
                        bitsWritten += bits;
                        //            System.out.println((cont++)+") code: "+code+", ext: "+ext);
                        }
                }
                
                } else { /* Long blocks */
                //int [] scalefac = sfBandIndex[3][0];
                int scalefac_index = 100;
                
                if ( gi.mixed_block_flag != 0 ) {
                    region1Start = 36;
                    region2Start = 576;
                } else {
                    scalefac_index = gi.region0_count + 1;
                    region1Start = scalefac_band_long[ scalefac_index ];
                    scalefac_index += gi.region1_count + 1;
                    region2Start = scalefac_band_long[ scalefac_index ];
                }
                
                for (int i = 0; i < bigv; i += 2 ) {
                    int tableindex = 100;
                    if ( i < region1Start ) {
                        tableindex = gi.table_select[0];
                    } else
                        if ( i < region2Start ) {
                        tableindex = gi.table_select[1];
                        } else {
                        tableindex = gi.table_select[2];
                        }
                    
                    /* get huffman code */
                    x = ix[i];
                    y = ix[i + 1];
                    if ( tableindex!= 0 ) {
                        bits = HuffmanCode( tableindex, x, y );
                        mn.add_entry( code, cbits );
                        mn.add_entry( ext, xbits );
                        //              System.out.println((cont++)+") code: "+code+", ext: "+ext);
                        bitsWritten += bits;
                    } else {
                        tablezeros += 1;
                    }
                }
                
                }
        }
        bvbits = bitsWritten;
        
        /* 2: Write count1 area */
        int tableindex = gi.count1table_select + 32;
        //    System.out.println("");
        //System.out.println("big_values prima di scrittura:  " + bigvalues);
//        System.out.println("big_value region prima di codifica: " + bigv);
//        System.out.println("gi.count1 in codifica: " + count1);
        count1End = bigv + count1;
//        System.out.println("coun1End: " + count1End);
        for (int i = bigv; i < count1End; i += 4 ) {
            v = ix[i];
            w = ix[i+1];
            x = ix[i+2];
            y = ix[i+3];
            bitsWritten += huffman_coder_count1(tableindex);
        }
        
//        c1bits = bitsWritten - bvbits;
//        if ( (stuffingBits = gi.part2_3_length - gi.part2_length - bitsWritten) != 0 ) {
//            int stuffingWords = stuffingBits / 32;
//            int remainingBits = stuffingBits % 32;
//
//            /*
//            Due to the nature of the Huffman code
//            tables, we will pad with ones
//             */
//            while ( stuffingWords-- != 0){
//                mn.add_entry( -1, 32 );
//            }
//            if ( remainingBits!=0 )
//                mn.add_entry( -1, remainingBits );
//            bitsWritten += stuffingBits;
//
//        }
        return bitsWritten;
    }
    
    
    
    
    
// private int huffman_coder_count1( BF_PartHolder pph, int tableindex, int v, int w, int x, int y ) {
    private int huffman_coder_count1( int tableindex ) {
        int huffbits;
        int signv, signw, signx, signy, p = 0;
        int len;
        int totalBits = 0;
        
        signv = (v > 0)? 0: 1;
        signw = (w > 0)? 0: 1;
        signx = (x > 0)? 0: 1;
        signy = (y > 0)? 0: 1;
//        if(v < 0) v = -v;
//        if(w < 0) w = -w;
//        if(x < 0) x = -x;
//        if(y < 0) y = -y;
        v = Math.abs(v);
        w = Math.abs(w);
        x = Math.abs(x);
        y = Math.abs(y);
        
        p = v + (w << 1) + (x << 2) + (y << 3);
        
//
//        System.out.println(p);
//        System.out.println(tableindex);
//        System.out.println("table.len: "+table.length);
//        System.out.println("table[table].len: "+table[tableindex].length);
        huffbits = table[tableindex][p];
        len = hlen[tableindex][ p ];
        mn.add_entry( huffbits, len );
        //System.out.println((cont++)+") count 1: "+huffbits);
        totalBits += len;
        if ( v != 0 ) {
            mn.add_entry( signv, 1 );
            //  System.out.println((cont++)+") count 1 (segno): "+signv);
            totalBits += 1;
        }
        if ( w != 0 ) {
            mn.add_entry( signw, 1 );
            //System.out.println((cont++)+") count 1 (segno): "+signw);
            totalBits += 1;
        }
        
        if ( x != 0 ) {
            mn.add_entry( signx, 1 );
            //System.out.println((cont++)+") count 1 (segno): "+signx);
            totalBits += 1;
        }
        if ( y != 0 ) {
            mn.add_entry( signy, 1 );
            //System.out.println((cont++)+") count 1 (segno): "+signy);
            totalBits += 1;
        }
        return totalBits;
    }
    
    
    /* Implements the pseudocode of page 98 of the IS */
    int HuffmanCode(int table_select, int x, int y ) {
        int signx = 0, signy = 0, linbitsx, linbitsy, linbits, xlen, ylen, idx;
        
        cbits = 0;
        xbits = 0;
        code  = 0;
        ext   = 0;
        
        if(table_select==0) return 0;
        
//        signx = (x > 0)? 0: 1;
//        signy = (y > 0)? 0: 1;
        //x = Math.abs( x );
        //y = Math.abs( y );
        
        if(x < 0) {x = -x; signx = 1;}
        if(y < 0) {y = -y; signy = 1;}
        
        xlen = this.xlen[table_select];
        ylen = this.ylen[table_select];
        linbits = this.linbits[table_select];
        linbitsx = linbitsy = 0;
        
        if ( table_select > 15 ) { /* ESC-table is used */
            if ( x > 14 ) {
                linbitsx = x - 15;
                x = 15;
            }
            if ( y > 14 ) {
                linbitsy = y - 15;
                y = 15;
            }
            
            idx = (x * ylen) + y;
            code  = table[table_select][idx];
            cbits = hlen [table_select][idx];
            if ( x > 14 ) {
                ext   |= linbitsx;
                xbits += linbits;
            }
            if ( x != 0 ) {
                ext <<= 1;
                ext |= signx;
                xbits += 1;
            }
            if ( y > 14 ) {
                ext <<= linbits;
                ext |= linbitsy;
                xbits += linbits;
            }
            if ( y != 0 ) {
                ext <<= 1;
                ext |= signy;
                xbits += 1;
            }
        } else { /* No ESC-words */
            idx = (x * ylen) + y;
            code = table[table_select][idx];
            cbits += hlen[table_select][ idx ];
            
            if ( x != 0 ) {
                code <<= 1;
                code |= signx;
                cbits ++;
            }
            if ( y != 0 ) {
                code <<= 1;
                code |= signy;
                cbits ++;
            }
        }
        
        return cbits + xbits;
    }
    
    
    
    private static int ResvSize ; /* in bits */
    private static int ResvMax  ; /* in bits */
    /*--------------------------------------------------
     *  reservoir
     */
    
    
    private void ResvFrameBegin( /*frame_params *fr_ps, */SideInfoEnc l3_side, int mean_bits, int frameLength ) {
        //layer info;
        int fullFrameBits, mode_gr;
        int resvLimit;
        /*
        info = fr_ps->header;
        if ( info->version == 1 ) {*/
        mode_gr = GR_MAX;
        resvLimit = 4088; /* main_data_begin has 9 bits in MPEG 1 */
        //resvLimit = (8 * 256) * mode_gr - 8;
            /*
        } else {
            mode_gr = 1;
            resvLimit = 2040; /* main_data_begin has 8 bits in MPEG 2 */
        // }
        
        fullFrameBits = mean_bits * mode_gr;
        
    /*
      determine maximum size of reservoir:
      ResvMax + frameLength <= 7680;
     */
        if ( frameLength > 7680 )
            ResvMax = 0;
        else
            ResvMax = 7680 - frameLength;
        
    /*
      limit max size to resvLimit bits because
      main_data_begin cannot indicate a
      larger value
     */
        if ( ResvMax > resvLimit )
            ResvMax = resvLimit;
        /*
        System.out.println("RESV MAX: "+ ResvMax);
        System.out.println("ResvSize all'inizio: " + ResvSize);
        System.out.println("");*/
    }
    
    
    
    void ResvAdjust(EChannel gi, int mean_bits, int channels ) {
        //System.out.println("mean_bits: " + mean_bits);
        //System.out.println("channels: " + channels);
        //System.out.println("part2_3_length: " + gi.part2_3_length);
        //System.out.println("");
        ResvSize += ((mean_bits / channels) - gi.part2_3_length);
    }
    
/*    void ResvFrameEnd(SideInfoEnc si, int mean_bits, int channels ) {
        EChannel gi;
 
        gi = si.ch[0].ch[0];
        gi.part2_3_length += ResvSize;
        ResvSize = 0;
    }*/
    
    void  ResvFrameEnd( SideInfoEnc l3_side, int mean_bits, int channels ) {
        
        EChannel gi;
        int mode_gr, gr, ch, ancillary_pad, stuffingBits;
        int over_bits;
        
        //info   = fr_ps->header;
        
        mode_gr = 2;
        ancillary_pad = 0;
        
        
        //         just in case mean_bits is odd, this is necessary...
        if ( (channels == 2) && (mean_bits & 1)!=0 )
            ResvSize += 1;
        //System.out.println("");
        //System.out.println("ResvSize: " + ResvSize);
        //System.out.println("ResvMax: " + ResvMax);
        //  System.out.println("mean_bits: " + mean_bits);
        //  System.out.println("stereo: " + channels);
        
        over_bits = ResvSize - ResvMax;
        if ( over_bits < 0 )
            over_bits = 0;
        
        
        ResvSize -= over_bits;
        stuffingBits = over_bits + ancillary_pad;
        
        // we must be byte aligned
        if ( (over_bits = ResvSize % 8)!=0 ) {
            stuffingBits += over_bits;
            ResvSize -= over_bits;
        }
        /*System.out.println("over_bits: " + over_bits);
        System.out.println("ResvSize: " + ResvSize);
        System.out.println("stuffingbits: " + stuffingBits);*/
        
        //stuffingBits = Math.abs(stuffingBits);
        if ( stuffingBits != 0 ) {
            
//              plan a: put all into the first granule
//              This was preferred by someone designing a
//              real-time decoder...
//
            gi = l3_side.gr[0].ch[0];
            
            if ( gi.part2_3_length + stuffingBits < 4095 ){
                gi.part2_3_length += stuffingBits;
                //gi.part2_3_length += ResvSize;
                //System.out.println("ramo A");
            } else {
                // plan b: distribute throughout the granules
                //System.out.println("ramo B");
                for (gr = 0; gr < mode_gr; gr++ )
                    for (ch = 0; ch < channels; ch++ ) {
                    int extraBits, bitsThisGr;
                    gi =  l3_side.gr[gr].ch[ch];
                    if ( stuffingBits == 0 )
                        break;
                    extraBits = 4095 - gi.part2_3_length;
                    bitsThisGr = extraBits < stuffingBits ? extraBits : stuffingBits;
                    gi.part2_3_length += bitsThisGr;
                    stuffingBits -= bitsThisGr;
                    }
            }
            
            
        }
        
        
        //  ResvSize = 0;
    }
    
    
    
    
    
/*
        ResvFrameEnd:
        Called after all granules in a frame have been allocated. Makes sure
        that the reservoir size is within limits, possibly by adding stuffing
        bits. Note that stuffing bits are added by increasing a granule's
        part2_3_length. The bitstream formatter will detect this and write the
        appropriate stuffing bits to the bitstream.
 */
    /*void ResvFrameEnd(SideInfoEnc  l3_side, int mean_bits,  int channels) {
     
        int						mode_gr, ch, ch, stereo, ancillary_pad, stuffingBits;
        int						over_bits;
     
        //info    = fr_ps->header;
        stereo  = channels;
        mode_gr = 2;
     
        ancillary_pad = 0;
     
     
        if ((stereo == 2)  &&  (mean_bits & 1) != 0)
            ResvSize ++;
     
        stuffingBits = ancillary_pad;
     
        if ((over_bits = ResvSize - ResvMax) > 0) {
            stuffingBits += over_bits;
            ResvSize     -= over_bits;
        }
     
        if ((over_bits = ResvSize % 8) != 0) {
            stuffingBits += over_bits;
            ResvSize     -= over_bits;
        }
     
        if (stuffingBits != 0) {
//
//                        plan a: put all into the first granule
//                        This was preferred by someone designing a
//                        real-time decoder...
//
            EChannel cod_info = l3_side.ch[0].ch[0];
     
            if (cod_info.part2_3_length + stuffingBits < 4095)
                cod_info.part2_3_length += stuffingBits;
            else {
                // plan b: distribute throughout the granules
                for (ch = 0;  ch < mode_gr;  ch++) {
                    for (ch = 0;  ch < stereo;  ch++) {
                        int			extraBits, bitsThisGr;
     
                        cod_info = l3_side.ch[0].ch[ch];
     
                        if (stuffingBits == 0)
                            break;
                        extraBits = 4095 - cod_info.part2_3_length;
                        bitsThisGr = (extraBits < stuffingBits) ? extraBits : stuffingBits;
                        cod_info.part2_3_length += bitsThisGr;
                        stuffingBits -= bitsThisGr;
                    }
                }
     
//                                If any stuffing bits remain, we elect to spill them
//                                into ancillary data. The bitstream formatter will do this if
//                                l3side->resvDrain is set
     
                //l3_side->resvDrain = stuffingBits;
            }
        }
    }*/
    
    
    
/*
  ResvMaxBits:
  Called at the beginning of each granule to get the max bit
  allowance for the current granule based on reservoir size
  and perceptual entropy.
 */
   /* private int ResvMaxBits(SideInfoEnc l3_side, double pe, int mean_bits ) {
        int more_bits, max_bits, add_bits, over_bits;
    
        mean_bits /= 2;
        max_bits = mean_bits;
    
        if ( max_bits > 4095 )
            max_bits = 4095;
    
        if ( ResvMax == 0 )
            return max_bits;
    
        more_bits = (int)(pe * 3.1 - mean_bits);
        add_bits = 0;
        if ( more_bits > 100 ) {
            int frac = (ResvSize * 6) / 10;
    
            if ( frac < more_bits )
                add_bits = frac;
            else
                add_bits = more_bits;
        }
        over_bits = ResvSize - ((ResvMax * 8) / 10) - add_bits;
        if ( over_bits > 0 )
            add_bits += over_bits;
    
        max_bits += add_bits;
        if ( max_bits > 4095 )
            max_bits = 4095;
        return max_bits;
    }
    */
}

