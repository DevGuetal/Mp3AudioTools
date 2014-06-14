package org.guetal.mp3.processing.encoder;


/**
 *  The following class generates a 4-bytes array specificating the mp3 frame headerstring.
 *
 *  @return headerstring    array containing information about mp3 frame.
 *  @author Thomas Gualino
 */

public final class HeaderEnc {
    protected int headerstring = 0;
    protected final int LAYER = 3;
    
    protected static int version;
    protected final int protection_bit = 1;
    protected int bitrate;
    protected int fs;
    protected String mode;
    protected int mode_extension;
    protected boolean copyright = false;
    protected int emphasis;
    protected int channels = 2;
    protected int padding_bit = 0;
    
    /**
     * Creates a new instance of HeaderEnc
     *
     *
     * @param version         mpeg version (1 or 2)
     * @param bitrate         mp3 bitrate (fps)
     * @param mode            string containg mode: "stereo", "joint_stereo", "dual_channel", "single_channel"
     * @param intensity_stereo boolean indicating if intensity stereo is used
     * @param MS_stereo       boolean indicatin if MS_stereo is used
     * @param emphasis         idicate the type of emphasis is used. Can assume the following values: 0 => no emphasis; 1 => 50/15 microsec. emphasis; 3 => CCITT J.17
     */
    public HeaderEnc(int version, int bitrate, int fs, String mode, 
            int mode_extension, 
            int emphasis, int padding_bit) {
        this.version = version;
        this.bitrate = bitrate;
        this.fs = fs;
        this.mode = mode;
        
        if(mode.equals("single_channel"))
            this.channels = 1;
        else
            this.channels = 2;

        this.mode_extension = mode_extension;
        this.emphasis = emphasis;
        this.padding_bit = padding_bit;
    }
    
    public void setBitRate(int bitrate){
        this.bitrate = bitrate;
    }
    
    /** Stores data of header in a byte array
     *
     * @return a 4-bytes array containing header information
     */
    public byte [] format_header(){
        generate_syncword();
        generate_version();
        generate_layer();
        generate_protection_bit();
        generate_bitrate();
        generate_sr();
        generate_padding();
        generate_mode();
        generate_mode_extention();
        generate_emphasis_field();
        
        return intToByteArray(headerstring);
    }

    /*
    public void set_padding(int padding){
        this.padding = padding;
    }*/
   
    private void generate_syncword(){
        headerstring = 0xfff00000;
    }
    
    private void generate_layer(){
        if(LAYER == 3)
            headerstring |= 0x20000;
    }
    
    private void generate_padding() {
        if(padding_bit != 0)
            headerstring |= 0x200;
    }
    
    private void generate_protection_bit() {
        if( protection_bit != 0)
            headerstring |= 0x10000;
    }
    
    private void generate_version() {
        if(version == 1)
            headerstring |= 0x80000;
    }
    
    private void generate_bitrate() {
        switch(bitrate){
            case 32000: headerstring |= 0x1000; break;
            case 40000: headerstring |= 0x2000; break;
            case 48000: headerstring |= 0x3000; break;
            case 56000: headerstring |= 0x4000; break;
            case 64000: headerstring |= 0x5000; break;
            case 80000: headerstring |= 0x6000; break;
            case 96000: headerstring |= 0x7000; break;
            case 112000: headerstring |= 0x8000; break;
            case 128000: headerstring |= 0x9000; break;
            case 160000: headerstring |= 0xa000; break;
            case 192000: headerstring |= 0xb000; break;
            case 224000: headerstring |= 0xc000; break;
            case 256000: headerstring |= 0xd000; break;
            case 320000: headerstring |= 0xe000; break;
        }
    }
    
    private void generate_sr() {
        switch(fs){
            case 48000: headerstring |= 0x400; break;
            case 32000: headerstring |= 0x800; break;
            default: break;
        }
    }
    
    private void generate_mode() {
        
        if(mode.equals("joint_stereo"))
            headerstring |= 0x40;
        
        if(mode.equals("dual_channel"))
            headerstring |= 0x80;
        
        if(mode.equals("single_channel"))
            headerstring |= 0xc0;
    }
    
    private void generate_mode_extention() {

            headerstring |= (mode_extension << 4);
    }
    
    private void generate_emphasis_field() {
//        switch(emphasis){
//            case 1: headerstring |= 0x1; break;
//            case 3: headerstring |= 0x3; break;
//            default: break;
//        }
        headerstring |= emphasis;
    }
    
    
    private byte[] intToByteArray(int integer) {
        byte[] byteArray = new byte[4];
        int byteNum = 4;
        
        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (integer >>> (n * 8));
        
        return (byteArray);
    }
    
    /**
     * Getter method for mp3 headerstring
     *
     * @return    headerstring
     */
    public byte [] get_headerstring(){
        
        byte [] ans = intToByteArray(headerstring);
        return ans;
    }
    
    
    /** Getter for bitrate
     *
     * @return bitrate in bits
     */
    public int get_bitrate(){
        return this.bitrate;
    }
    
    
    /** Getter for fs
     *
     * @return sampling frequency
     */
    public int get_fs(){
        return this.fs;
    }
}
